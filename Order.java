import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Order {
	private JFrame frame;
	private DefaultTableModel model;
	private JTable orderTable;

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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Order();
			}
		});
	}

	public Order() {
		Connect();
		initialize();
		refreshOrderTable();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 505, 387);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// Adding a JScrollPane to the JTable
		JScrollPane scrollPane = new JScrollPane(orderTable);
		scrollPane.setBounds(10, 10, 414, 180);
		frame.getContentPane().add(scrollPane);

		




		// Adding a button to add an order
		JButton addButton = new JButton("Add Order");
		addButton.setBounds(10, 240, 117, 29);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addOrderGUI();
			}
		});
		frame.getContentPane().add(addButton);

		// Adding a button to delete an order
		JButton deleteButton = new JButton("Delete Order");
		deleteButton.setBounds(137, 240, 140, 29);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteOrder();
			}
		});
		frame.getContentPane().add(deleteButton);

		// Adding a button to refresh the order table
		JButton viewButton = new JButton("View");
		viewButton.setBounds(307, 240, 117, 29);
		viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshOrderTable();
			}
		});


		frame.getContentPane().add(viewButton);

		// Creating DefaultTableModel and JTable
		model = new DefaultTableModel();
		model.addColumn("OrderID");
		model.addColumn("TotalAmount");
		model.addColumn("Date");
		model.addColumn("IsPaid");
		model.addColumn("CustomerId");


		orderTable = new JTable(model);
		scrollPane.setViewportView(orderTable);
		
		

		// Make the frame visible
		frame.setVisible(true);
	}

	private void refreshOrderTable() {
		DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
		model.setRowCount(0); // Clear existing rows in the table

		try {
			// Prepare the SQL statement to retrieve all orders from the database
			String sql = "SELECT * FROM ordes";
			pst = con.prepareStatement(sql);

			// Execute the SQL statement to retrieve all orders from the database
			rs = pst.executeQuery();

			// Iterate over the result set and add each order to the table
			while (rs.next()) {
				int orderId = rs.getInt("OrderID");
				float TotalAmount = rs.getFloat("TotalAmount");
				Date DateOfOrder = rs.getDate("DATE");
				boolean IsPaid= rs.getBoolean("IsPaid");
				String Customer_CustomerId = rs.getString("Customer_CustomerId");


				// Add the order details to the table
				Object[] row = {orderId,TotalAmount,DateOfOrder,IsPaid,Customer_CustomerId};
				model.addRow(row);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, "Error refreshing ordes table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			// Close the ResultSet and PreparedStatement
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(frame, "Error refreshing ordes table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}



	private void addOrder(int orderId,float totalAmount,Date date,int customerId,boolean isPaid) {
		
        try {
            String sql = "INSERT INTO ordes (orderId, TotalAmount, DATE, Customer_CustomerId, isPaid) VALUES (?, ?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, orderId);
            pst.setFloat(2, totalAmount);
            pst.setDate(3, date);
            pst.setInt(4, customerId);
            pst.setBoolean(5, isPaid);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Order added successfully.");
            
            refreshOrderTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error adding order: " + ex.getMessage());
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                	 JOptionPane.showMessageDialog(frame, "Error adding order: " + ex.getMessage());
                }
            }
        }
        
	}
	private void addOrderGUI() {
	    JComboBox<Integer> customerComboBox = new JComboBox<>();
	    JTextField orderIdField = new JTextField();
	    JTextField totalAmountField = new JTextField();
	    JTextField dateField = new JTextField();
	    JCheckBox isPaidField = new JCheckBox();

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
	            "Order ID:", orderIdField,
	            "Total Amount:", totalAmountField,
	            "Date (YYYY-MM-DD):", dateField,
	            "Customer ID:", customerComboBox,
	            "Is Paid:", isPaidField
	    };

	    int option = JOptionPane.showConfirmDialog(null, message, "Add Order", JOptionPane.OK_CANCEL_OPTION);
	    if (option == JOptionPane.OK_OPTION) {
	        int orderId = Integer.parseInt(orderIdField.getText());
	        float totalAmount = Float.parseFloat(totalAmountField.getText());
	        Date date = Date.valueOf(dateField.getText());
	        int customerId = (int) customerComboBox.getSelectedItem();
	        boolean isPaid = isPaidField.isSelected();

	        addOrder(orderId, totalAmount, date, customerId, isPaid);
	        JOptionPane.showMessageDialog(frame, "Order added successfully.");
	    }
	}

	private void deleteOrder() {
		int orderId = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Order ID:"));

		if (orderId != -1) {//Assume that -1 Can't be an ID

			try {
				// Prepare the SQL statement to delete the order from the database
				String sql = "DELETE FROM ordes WHERE orderID = ?";
				pst = con.prepareStatement(sql);

				// Set the value for the parameter in the SQL statement
				pst.setInt(1, orderId);

				// Execute the SQL statement to delete the order from the database
				pst.executeUpdate();

				JOptionPane.showMessageDialog(frame, "Order deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

				// Refresh the order table to remove the deleted order
				refreshOrderTable();

			} catch (SQLException e) {
				JOptionPane.showMessageDialog(frame, "Error deleting order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				// Close the PreparedStatement
				if (pst != null) {
					try {
						pst.close();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame, "Error deleting order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

					}
				}
			}
		} else {
			JOptionPane.showMessageDialog(frame, "Please select an order to delete.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
