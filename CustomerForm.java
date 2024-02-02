import javax.swing.*;

import java.sql.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class CustomerForm {
    private JFrame frame;
    private JTextField customerIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField emailField;
    private JTextField phoneField;
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

    public CustomerForm() {
    	Connect();
    	 initialize();
    }
    
    private void initialize() { 
    frame = new JFrame("Add Customer");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(568, 487);

    JPanel panel = new JPanel();

    customerIdField = new JTextField(10);
    customerIdField.setBounds(132, 99, 368, 24);
    firstNameField = new JTextField(10);
    firstNameField.setBounds(132, 150, 368, 23);
    lastNameField = new JTextField(10);
    lastNameField.setBounds(132, 210, 368, 24);
    addressField = new JTextField(10);
    addressField.setBounds(132, 271, 368, 24);
    emailField = new JTextField(10);
    emailField.setBounds(132, 329, 368, 24);
    phoneField = new JTextField(10);
    phoneField.setBounds(132, 383, 368, 24);
    panel.setLayout(null);

    JLabel CustomerID = new JLabel("Customer ID:");
    CustomerID.setBounds(20, 104, 95, 14);
    panel.add(CustomerID);
    panel.add(customerIdField);
    JLabel First = new JLabel("First Name:");
    First.setBounds(20, 154, 87, 14);
    panel.add(First);
    panel.add(firstNameField);
    JLabel Last = new JLabel("Last Name:");
    Last.setBounds(21, 215, 86, 14);
    panel.add(Last);
    panel.add(lastNameField);
    JLabel Address = new JLabel("Address:");
    Address.setBounds(34, 276, 73, 14);
    panel.add(Address);
    panel.add(addressField);
    JLabel Email = new JLabel("Email:");
    Email.setBounds(58, 334, 60, 14);
    panel.add(Email);
    panel.add(emailField);
    JLabel Phone = new JLabel("Phone:");
    Phone.setBounds(58, 388, 64, 14);
    panel.add(Phone);
    panel.add(phoneField);

    JButton addButton = new JButton("Add Customer");
    addButton.setBounds(132, 418, 134, 23);
    addButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            addCustomer();
        }
    });
	
    panel.add(addButton);

    frame.getContentPane().add(panel);
    
    JButton btnCancel = new JButton("Cancel");
    btnCancel.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		frame.dispose();
    	}
    });
    
    btnCancel.setBounds(346, 418, 101, 23);
    panel.add(btnCancel);
    
    JLabel lblNewLabel = new JLabel("Enter the following:");
    lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
    lblNewLabel.setBounds(147, 49, 225, 39);
    panel.add(lblNewLabel);
    frame.setVisible(true);
    }

    private void addCustomer() {
        try {
            int customerId = Integer.parseInt(customerIdField.getText());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String address = addressField.getText();
            String email = emailField.getText();
            int phone = Integer.parseInt(phoneField.getText());


			// Prepare the SQL statement to insert the customer details into the database
			String sql = "INSERT INTO customer (CustomerId, FirstName,LastName,Address,Email,Phone ) VALUES (?,?,?,?,?,?)";
			pst = con.prepareStatement(sql);
			pst.setInt(1, customerId);
			pst.setString(2, firstName);
			pst.setString(3, lastName);
			pst.setString(4, address);
			pst.setString(5, email);
			pst.setInt(6, phone);



			// Execute the SQL statement to insert the customer details
			pst.executeUpdate();

			// Refresh the customer table to reflect the changes
			

			// Show a success message to the user
			JOptionPane.showMessageDialog(frame, "Customer added successfully.");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, "Error adding customer: " + e.getMessage());
		} catch(Exception e ) {
			JOptionPane.showMessageDialog(frame, "Error adding customer: " + e.getMessage());
		}finally {
			// Close the PreparedStatement
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CustomerForm();
            }
        });
    }
}
