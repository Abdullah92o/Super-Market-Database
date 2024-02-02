import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoyaltyCard {

    private JFrame frame;
    private DefaultTableModel model;
    private JTable loyaltyCardTable;
    
    

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;


    public void Connect() {
        String url = "jdbc:mysql://localhost:3306/supermarket";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            System.out.println("Connected to MySQL database.");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoyaltyCard();
            }
        });
    }

    /**
     * Create the application.
     */
    public LoyaltyCard() {

        Connect();
        initialize();
        refreshLoyaltyCardTable();

    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        frame = new JFrame();
        frame.setBounds(100, 100, 506, 374);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Adding a JScrollPane to the JTable
        JScrollPane scrollPane = new JScrollPane(loyaltyCardTable);
        scrollPane.setBounds(10, 10, 459, 266);
        frame.getContentPane().add(scrollPane);

        

        // Populate the customer combo box with existing customers
     

        // Adding a button to add a loyalty card
        JButton addButton = new JButton("Add Loyalty Card");
        addButton.setBounds(10, 287, 150, 29);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	addLoyaltyCardGUI();
            }
        });
        frame.getContentPane().add(addButton);

        // Adding a button to delete a loyalty card
        JButton deleteButton = new JButton("Delete Loyalty Card");
        deleteButton.setBounds(179, 287, 170, 29);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteLoyaltyCard();
            }
        });
        frame.getContentPane().add(deleteButton);

        // Adding a button to refresh the loyalty card table
        JButton viewButton = new JButton("View");
        viewButton.setBounds(387, 287, 70, 29);
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshLoyaltyCardTable();
            }
        });
        frame.getContentPane().add(viewButton);

        // Creating DefaultTableModel then JTable
        model = new DefaultTableModel();
        model.addColumn("CardNumber");
        model.addColumn("Points");
        model.addColumn("Customer_CustomerId");

        loyaltyCardTable = new JTable(model);
        scrollPane.setViewportView(loyaltyCardTable);
        
    
        // Make the frame visible
        frame.setVisible(true);

    }

    private void refreshLoyaltyCardTable() {
        DefaultTableModel model = (DefaultTableModel) loyaltyCardTable.getModel();
        model.setRowCount(0); // Clear existing rows in the table

        try {
            // Prepare the SQL statement to retrieve all loyalty cards from the database
            String sql = "SELECT * FROM loyaltycard";
            pst = con.prepareStatement(sql);

            // Execute the SQL statement to retrieve all loyalty cards from the database
            rs = pst.executeQuery();

            // Iterate over the result set and add each loyalty card to the table
            while (rs.next()) {
                int cardNumber = rs.getInt("CardNumber");
                int points = rs.getInt("Points");
                int customerId = rs.getInt("Customer_CustomerId");

                // Add the loyalty card details to the table
                Object[] row = {cardNumber, points, customerId};
                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error refreshing loyalty card table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close the ResultSet and PreparedStatement
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(frame, "Error refreshing loyalty card table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

   

    private void addLoyaltyCard(int cardNumber ,int points,int customerId ) {
    	try {
            String sql = "INSERT INTO loyaltycard (CardNumber, Points, Customer_CustomerId) VALUES (?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, cardNumber);
            pst.setInt(2, points);
            pst.setInt(3, customerId);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Loyalty card added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add loyalty card.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error adding loyalty card: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    private void addLoyaltyCardGUI() {
        JTextField pointsFiled = new JTextField();
        JComboBox<Integer> customerComboBox = new JComboBox<>();

        // Populate the customer combo box
        try {
            String sql = "SELECT CustomerId FROM customer";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                int customerId = rs.getInt("CustomerId");
                customerComboBox.addItem(customerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[] message = {
                "Enter Points :", pointsFiled,
                "Select Customer ID:", customerComboBox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Loyalty Card", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int cardNumber = generateCardNumber();
            int points = Integer.parseInt(pointsFiled.getText());
            int customerId = (int) customerComboBox.getSelectedItem();

            addLoyaltyCard(cardNumber, points, customerId);
            refreshLoyaltyCardTable();
        }
    }
    
    private int generateCardNumber() {
        int cardNumber = 0;

        try {
            // Prepare the SQL statement to retrieve the highest card number from the database
            String sql = "SELECT MAX(CardNumber) AS MaxCardNumber FROM loyaltycard";
            pst = con.prepareStatement(sql);

            // Execute the SQL statement to retrieve the highest card number from the database
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                cardNumber = rs.getInt("MaxCardNumber") + 1;
            } else {
                cardNumber = 1;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error generating card number: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close the ResultSet and PreparedStatement
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(frame, "Error generating card number: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        return cardNumber;
    }

    private void deleteLoyaltyCard() {
        int selectedRow = loyaltyCardTable.getSelectedRow();

        if (selectedRow != -1) {
            int cardNumber = (int) loyaltyCardTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the selected loyalty card?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Prepare the SQL statement to delete the selected loyalty card from the database
                    String sql = "DELETE FROM loyaltycard WHERE CardNumber = ?";
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, cardNumber);

                    // Execute the SQL statement to delete the selected loyalty card from the database
                    int rowsAffected = pst.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(frame, "Loyalty card deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshLoyaltyCardTable(); // Refresh the loyalty card table to remove the deleted card
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to delete loyalty card.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(frame, "Error deleting loyalty card: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Close the PreparedStatement
                    if (pst != null) {
                        try {
                            pst.close();
                        } catch (SQLException e) {
                        	JOptionPane.showMessageDialog(frame, "Error deleteLoyaltyCard : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a loyalty card to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    
}
