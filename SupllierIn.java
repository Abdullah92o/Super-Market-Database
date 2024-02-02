import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SupllierIn {

    private JFrame frame;
    private DefaultTableModel model;
    private JTable supplierTable;
    
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SupllierIn();
            }
        });
    }

    public SupllierIn() {
        initialize();
        Connect();
        viewSupplierTable();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 612, 447);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        

        JScrollPane scrollPane = new JScrollPane(supplierTable);
        scrollPane.setBounds(10, 10, 580, 340);
        frame.getContentPane().add(scrollPane);

        JButton addButton = new JButton("Add Supplier");
        addButton.setBounds(36, 361, 117, 29);
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSupplier();
            }
        });
        frame.getContentPane().add(addButton);

        JButton deleteButton = new JButton("Delete Supplier");
        deleteButton.setBounds(220, 361, 140, 29);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSupplier();
            }
        });
        frame.getContentPane().add(deleteButton);

        JButton viewButton = new JButton("View");
        viewButton.setBounds(429, 361, 117, 29);
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewSupplierTable();
            }
        });
        frame.getContentPane().add(viewButton);
        
        
        
        model = new DefaultTableModel();
	    model.addColumn("SupplierId");
	    model.addColumn("Email");
	    model.addColumn("ContactName");
	   

	    // Create the table and set the model
	    supplierTable = new JTable(model);
	    scrollPane.setViewportView(supplierTable);
	    

        frame.setVisible(true);
    }

    private void addSupplier() {
    	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SupplierForm();
            }
        });
    	
    }

    private void deleteSupplier() {
        String input = JOptionPane.showInputDialog(frame, "Enter the Supplier ID to delete:");
        if (input != null && !input.isEmpty()) {
            int supplierId = Integer.parseInt(input);
            try {
                String sql = "DELETE FROM supplier WHERE SupplierId = ?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, supplierId);

                pst.executeUpdate();

                viewSupplierTable();

                JOptionPane.showMessageDialog(frame, "Supplier deleted successfully.");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Error deleting supplier: " + e.getMessage());
            } finally {
                if (pst != null) {
                    try {
                        pst.close();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(frame, "Error deleting supplier: " + e.getMessage());

                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid supplier ID. Please try again.");
        }
    }

    private void viewSupplierTable() {
        DefaultTableModel model = (DefaultTableModel) supplierTable.getModel();
        model.setRowCount(0);

        try {
            String sql = "SELECT * FROM supplier";
            pst = con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int supplierId = rs.getInt("SupplierId");
                String email = rs.getString("Email");
                String contactName = rs.getString("ContactName");

                Object[] row = {supplierId, email, contactName, };
	            model.addRow(row);
	        }

	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(frame, "Error viewing customer table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    } finally {
	        // Close the ResultSet and PreparedStatement
	        if (pst != null) {
	            try {
	                pst.close();
	            } catch (SQLException e) {
	    	        JOptionPane.showMessageDialog(frame, "Error viewing customer table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }
	}
}
