import javax.swing.*;

import java.sql.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Dialog.ModalExclusionType;

public class SuperMarketGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton customerButton;
	private JButton supplierButton;
	private JButton productButton;
	private JButton warehouseButton;
	private JButton orderButton;
	private JButton loyaltyCardButton;
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	private JButton OrderProductManagerB;

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

	public SuperMarketGUI() {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setTitle("SuperMarket Database");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(620, 433);
		setLocationRelativeTo(null);

		// Create buttons

		customerButton = new JButton("Customers");
		customerButton.setBounds(120, 90, 120, 23);
		supplierButton = new JButton("Suppliers");
		supplierButton.setBounds(328, 90, 120, 23);
		productButton = new JButton("Products");
		productButton.setBounds(120, 124, 120, 23);
		warehouseButton = new JButton("Warehouses");
		warehouseButton.setBounds(328, 160, 120, 23);
		orderButton = new JButton("Orders");
		orderButton.setBounds(119, 160, 121, 23);
		loyaltyCardButton = new JButton("Loyalty Cards");
		loyaltyCardButton.setBounds(120, 235, 120, 23);

		// Add action listeners


		customerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open Customer Management window
				openCustomerManagement();
			}
		});

		supplierButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open Supplier Management window
				openSupplierManagement();
			}
		});

		productButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open Product Management window
				openProductManagement();
			}
		});

		warehouseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open Warehouse Management window
				openWarehouseManagement();
			}
		});

		orderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open Order Management window
				openOrderManagement();
			}
		});

		loyaltyCardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open Loyalty Card Management window
				openLoyaltyCardManagement();
			}
		});

		// Create a panel and add buttons
		JPanel panel = new JPanel();
		panel.setLayout(null);

		panel.add(customerButton);
		panel.add(supplierButton);
		panel.add(productButton);
		panel.add(warehouseButton);
		panel.add(orderButton);
		panel.add(loyaltyCardButton);

		// Add panel to the frame
		getContentPane().add(panel);
		
		JLabel lblNewLabel = new JLabel("Welcome to SuperMarket");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblNewLabel.setBounds(117, 45, 273, 34);
		panel.add(lblNewLabel);
		
		JButton SuppllierProduct = new JButton("Supplier Suplies Product");
		SuppllierProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oprnSupplierProductManagement();
			}
		});
		
		SuppllierProduct.setBounds(328, 124, 182, 23);
		panel.add(SuppllierProduct);
		
		OrderProductManagerB = new JButton("Order contain Product");
		OrderProductManagerB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 openOrderProductManager();
			}
		});
		OrderProductManagerB.setBounds(120, 194, 182, 23);
		panel.add(OrderProductManagerB);

		setVisible(true);
	}



	private void openCustomerManagement() {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CustomerIn();
			}
		});

	}





	private void openSupplierManagement() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SupllierIn();
			}
		});
	}


	private void openProductManagement() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Product();
			}
		});

	}

	private void openWarehouseManagement() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new WarehouseIn();
			}
		});



	}

	private void openOrderManagement() {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Order();
            }
        });

	}

	private void openLoyaltyCardManagement() {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoyaltyCard();
            }
        });
	}
	
	private void oprnSupplierProductManagement() {
	SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            new SupplierProductManager();
        }
    });
	}
	
	private void openOrderProductManager() {
		 SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                new OrderProductManager();
	            }
	        });
		}





	public static void main(String[] args) {
		// Run the GUI on the Event Dispatch Thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SuperMarketGUI();
			}
		});
	}
}
