import javax.swing.*;

import java.sql.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class SupplierForm {
    private JFrame frame;
    private JTextField supplierIdField;
    private JTextField emailField;
    private JTextField contactNameField;

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    public void connect() {
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

    public SupplierForm() {
        connect();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Add Supplier");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        supplierIdField = new JTextField();
        supplierIdField.setBounds(150, 50, 200, 25);
        panel.add(supplierIdField);
        JLabel label = new JLabel("Supplier ID:");
        label.setBounds(50, 50, 100, 25);
        panel.add(label);

        emailField = new JTextField();
        emailField.setBounds(150, 100, 200, 25);
        panel.add(emailField);
        JLabel label_1 = new JLabel("Email:");
        label_1.setBounds(50, 100, 100, 25);
        panel.add(label_1);

        contactNameField = new JTextField();
        contactNameField.setBounds(150, 150, 200, 25);
        panel.add(contactNameField);
        JLabel label_2 = new JLabel("Contact Name:");
        label_2.setBounds(50, 150, 100, 25);
        panel.add(label_2);

        JButton addButton = new JButton("Add Supplier");
        addButton.setBounds(150, 200, 200, 25);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSupplier();
            }
        });
        panel.add(addButton);

        frame.getContentPane().add(panel);
        
        JLabel lblNewLabel = new JLabel("Enter the following:");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblNewLabel.setBounds(132, 11, 165, 28);
        panel.add(lblNewLabel);
        
        JButton Cancelbtn = new JButton("Cancel");
        Cancelbtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.dispose();
        	}
        });
        Cancelbtn.setBounds(150, 236, 200, 25);
        panel.add(Cancelbtn);
        frame.setVisible(true);
    }

    private void addSupplier() {
        int supplierId = Integer.parseInt(supplierIdField.getText());
        String email = emailField.getText();
        String contactName = contactNameField.getText();

        try {
            String sql = "INSERT INTO supplier (SupplierId, Email, ContactName) VALUES (?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, supplierId);
            pst.setString(2, email);
            pst.setString(3, contactName);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Supplier added successfully.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error adding supplier: " + ex.getMessage());
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error adding supplier: " + ex.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SupplierForm();
            }
        });
    }
}
