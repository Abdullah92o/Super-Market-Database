

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Product {//needs edit to deal with FK

	private JFrame frame;
	 private DefaultTableModel model;
	private JTable productTable;
	
	
	

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
				new Product();
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Product() {
		
		Connect();
		initialize();
		refreshProductTable();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 664, 374);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		

		// Adding a JScrollPane to the JTable
		JScrollPane scrollPane = new JScrollPane(productTable);
		scrollPane.setBounds(10, 10, 632, 227);
		frame.getContentPane().add(scrollPane);

		
		
		// Adding a button to add a product
		JButton addButton = new JButton("Add Product");
		addButton.setBounds(10, 279, 117, 29);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addProductGUI();
			}
		});
		frame.getContentPane().add(addButton);

		// Adding a button to delete a product
		JButton deleteButton = new JButton("Delete Product");
		deleteButton.setBounds(217, 279, 140, 29);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteProduct();
			}
		});
		frame.getContentPane().add(deleteButton);

		// Adding a button to refresh the product table
		   JButton viewButton = new JButton("View");
	        viewButton.setBounds(459, 279, 117, 29);
	        viewButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	refreshProductTable();
	            }
	        });
	        frame.getContentPane().add(viewButton);
	        
	     // Creating DefaultTableMode then JTable
	        
	        model = new DefaultTableModel();
		    model.addColumn("ProudctId");
		    model.addColumn("ProudctName");
		    model.addColumn("QuantityInStock");
		    model.addColumn("Category");
		    model.addColumn("Enddate");
		    model.addColumn("Price");
		    model.addColumn("WareHouse Adress");
		    
		    
	         
		    productTable = new JTable(model);
		    scrollPane.setViewportView(productTable);
		    
	

		// Make the frame visible
		frame.setVisible(true);

	}

 

	private void refreshProductTable() {
		DefaultTableModel model = (DefaultTableModel) productTable.getModel();
		model.setRowCount(0); // Clear existing rows in the table

		try {
			// Prepare the SQL statement to retrieve all products from the database
			String sql = "SELECT * FROM proudct";
			try {
			pst = con.prepareStatement(sql);
			}catch(Exception e) {
				System.out.println("proplem");
			}

			// Execute the SQL statement to retrieve all products from the database
			 rs = pst.executeQuery();

			// Iterate over the result set and add each product to the table
			while (rs.next()) {
				int productId = rs.getInt("ProudctId");
				String productName = rs.getString("ProudctName");
				int quantityInStock = rs.getInt("QuantityInStock");
				String category = rs.getString("Category");
				Date endDate = rs.getDate("Enddate");
				float price = rs.getFloat("Price");
				String warehouseAddress = rs.getString("Warehouse_Address");

				// Add the product details to the table
				Object[] row = {productId, productName, quantityInStock, category, endDate, price, warehouseAddress};
				model.addRow(row);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, "Error refreshing product table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			// Close the ResultSet and PreparedStatement
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(frame, "Error refreshing product table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	

	
	 private void addProduct(int productId, String productName,int quantityInStock ,String category,Date endDate,float price,String warehouseAddress) {
    
        try {
            String sql = "INSERT INTO proudct (ProudctId, ProudctName, QuantityInStock, Category, EndDate, Price, Warehouse_Address) VALUES (?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, productId);
            pst.setString(2, productName);
            pst.setInt(3, quantityInStock);
            pst.setString(4, category);
            pst.setDate(5, endDate);
            pst.setFloat(6, price);
            pst.setString(7, warehouseAddress);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Product added successfully.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error adding product: " + ex.getMessage());
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error adding product: " + ex.getMessage());
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                	 JOptionPane.showMessageDialog(frame, "Error adding product: " + ex.getMessage());
                }
            }
        }
		}
	
	private void addProductGUI() {
	    JTextField productIdField = new JTextField();
	    JTextField productNameField = new JTextField();
	    JTextField quantityField = new JTextField();
	    JTextField categoryField = new JTextField();
	    JTextField endDateField = new JTextField();
	    JTextField priceField = new JTextField();
	    JComboBox<String> warehouseComboBox = new JComboBox<>();

	    // Populate the warehouse combo box
	    try {
	        String sql = "SELECT Address FROM warehouse";
	        pst = con.prepareStatement(sql);
	        rs = pst.executeQuery();
	        while (rs.next()) {
	            String address = rs.getString("Address");
	            warehouseComboBox.addItem(address);
	        }
	    } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error adding product: " + e.getMessage());

	    }

	    Object[] message = {
	            "Product ID:", productIdField,
	            "Product Name:", productNameField,
	            "Quantity in Stock:", quantityField,
	            "Category:", categoryField,
	            "End Date (YYYY-MM-DD):", endDateField,
	            "Price:", priceField,
	            "Warehouse Address:", warehouseComboBox
	    };

	    int option = JOptionPane.showConfirmDialog(null, message, "Add Product", JOptionPane.OK_CANCEL_OPTION);
	    if (option == JOptionPane.OK_OPTION) {
	        int productId = Integer.parseInt(productIdField.getText());
	        String productName = productNameField.getText();
	        int quantityInStock = Integer.parseInt(quantityField.getText());
	        String category = categoryField.getText();
	        Date  endDate=null;
	        //catch Date Enterty erors
	       try {
	         endDate = Date.valueOf(endDateField.getText());
	       }catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "Error deleting product: " + e.getMessage());
			}
	       
	        float price = Float.parseFloat(priceField.getText());
	        String warehouseAddress = (String) warehouseComboBox.getSelectedItem();

	        addProduct(productId, productName, quantityInStock, category, endDate, price, warehouseAddress);
	        refreshProductTable();
	        
	    }
	}
	
   
	private void deleteProduct() {
		String input = JOptionPane.showInputDialog(frame, "Enter the product ID to delete:");
		if (input != null && !input.isEmpty()) {
			int productId = Integer.parseInt(input);
			try {
				// Prepare the SQL statement to delete the product from the database
				String sql = "DELETE FROM proudct WHERE ProudctId = ?";
				pst = con.prepareStatement(sql);
				pst.setInt(1, productId);

				// Execute the SQL statement to delete the product from the database
				pst.executeUpdate();

				// Refresh the product table to reflect the changes
				refreshProductTable();

				// Show a success message to the user
				JOptionPane.showMessageDialog(frame, "Product deleted successfully.");

			} catch (SQLException e) {
				JOptionPane.showMessageDialog(frame, "Error deleting product: " + e.getMessage());
			} finally {
				// Close the PreparedStatement
				if (pst != null) {
					try {
						pst.close();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame, "Error deleting product: " + e.getMessage());
					}
				}
			}
		} else {
			JOptionPane.showMessageDialog(frame, "Invalid product ID. Please try again.");
		}
	}


}
