

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.sql.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CustomerIn {

	private JFrame frame;
	private DefaultTableModel model;
	private JTable customerTable;

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CustomerIn();
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CustomerIn() {
		initialize();
		Connect();
		viewCustomerTable();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 694, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);


		// Adding a JScrollPane to the JTable
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(46, 11, 600, 353);
		frame.getContentPane().add(scrollPane);



		// Adding a button to add a customer
		JButton addButton = new JButton("Add Customer");
		addButton.setBounds(46, 390, 117, 29);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addCustomer();
			}
		});
		frame.getContentPane().add(addButton);

		// Adding a button to delete a customer
		JButton deleteButton = new JButton("Delete Customer");
		deleteButton.setBounds(240, 390, 140, 29);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteCustomer();
			}
		});
		frame.getContentPane().add(deleteButton);

		// Adding a button to refresh the customer table
		JButton viewButton = new JButton("View");
		viewButton.setBounds(482, 390, 117, 29);
		viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewCustomerTable();
			}
		});
		frame.getContentPane().add(viewButton);

		model = new DefaultTableModel();
		model.addColumn("Customer ID");
		model.addColumn("First Name");
		model.addColumn("Last Name");
		model.addColumn("Address");
		model.addColumn("Email");
		model.addColumn("Phone");

		// Create the table and set the model
		customerTable = new JTable(model);
		scrollPane.setViewportView(customerTable);

		// Make the frame visible
		frame.setVisible(true);

	}


	private void addCustomer() {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CustomerForm();
            }
        });
	}

	private void deleteCustomer() {
		String input = JOptionPane.showInputDialog(frame, "Enter the customer ID to delete:");
		if (input != null && !input.isEmpty()) {
			int customerId = Integer.parseInt(input);
			try {
				// Prepare the SQL statement to delete the customer from the database
				String sql = "DELETE FROM customer WHERE customerId = ?";
				pst = con.prepareStatement(sql);
				pst.setInt(1, customerId);

				// Execute the SQL statement to delete the customer from the database
				pst.executeUpdate();

				// Refresh the customer table to reflect the changes
				viewCustomerTable();

				// Show a success message to the user
				JOptionPane.showMessageDialog(frame, "Customer deleted successfully.");

			} catch (SQLException e) {
				JOptionPane.showMessageDialog(frame, "Error deleting customer: " + e.getMessage());
			}catch(Exception e ) {
				JOptionPane.showMessageDialog(frame, "Error deleting customer: " + e.getMessage());
			} finally {
				// Close the PreparedStatement
				if (pst != null) {
					try {
						pst.close();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame, "Error deleting customer: " + e.getMessage());
					}
				}
			}
		} else {
			JOptionPane.showMessageDialog(frame, "Invalid customer ID. Please try again.");
		}
	}

	private void viewCustomerTable() {

		try {
			model.setRowCount(0);

			// Execute SQL query to retrieve customer data
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM customer");

			// Iterate through the result set and add data to the table model
			while (rs.next()) {
				int customerId = rs.getInt("CustomerId");
				String firstName = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				String Address = rs.getString("Address");
				String email = rs.getString("Email");
				String phone = rs.getString("Phone");

				// Add a new row to the table model
				model.addRow(new Object[]{customerId, firstName, lastName,Address, email, phone});
			}

			// Close the result set and statement
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, "Error view customer: " + e.getMessage());
		}finally {
			// Close the PreparedStatement
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(frame, "Error view customer: " + e.getMessage());
				}
			}
		}
	}

}
