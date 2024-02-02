
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class OrderProductManager {
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private JFrame frame;
    private JTable orderProductTable;
    private DefaultTableModel model;

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

    public OrderProductManager() {
        connect();
        initializeGUI();
        viewOrderProducts();
    }

   
    private void initializeGUI() {
        frame = new JFrame("Order Product Manager");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(442, 355);
        frame.getContentPane().setLayout(null); // Set the layout to AbsoluteLayout
        
        // Create a table with default table model
        orderProductTable = new JTable();
        model = new DefaultTableModel();
        model.addColumn("Product ID");
        model.addColumn("Order ID");
        model.addColumn("Customer ID");
        model.addColumn("Quantity");
        orderProductTable.setModel(model);
        
        // Create a scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(orderProductTable);
        scrollPane.setBounds(10, 10, 380, 200); // Set the position and size of the scroll pane
        frame.getContentPane().add(scrollPane);
        
        // Create a button to add a new order product
        JButton addButton = new JButton("Add Order Product");
        addButton.setBounds(10, 220, 180, 30); // Set the position and size of the button
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSupplierProductGUI();
            }
        });
        frame.getContentPane().add(addButton);
        
        // Create a button to remove an order product
        JButton removeButton = new JButton("Remove Order Product");
        removeButton.setBounds(210, 220, 180, 30); // Set the position and size of the button
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSupplierProductGUI();
            }
        });
        frame.getContentPane().add(removeButton);
        
        // Create a button to view an order product
        JButton viewButton = new JButton("View Order Product");
        viewButton.setBounds(10, 260, 180, 30); // Set the position and size of the button
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	viewOrderProducts();
            }
        });
        frame.getContentPane().add(viewButton);
        
        frame.setVisible(true);
    }		
	


    public void addOrderProduct(int productID, int orderID, int customerID, String quantity) {
        try {
            String sql = "INSERT INTO `order contain proudct` (Proudct_ProudctId, Order_OrderId, Order_Custumer_CustomerId, Quantity) VALUES (?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, productID);
            pst.setInt(2, orderID);
            pst.setInt(3, customerID);
            pst.setString(4, quantity);
            pst.executeUpdate();
        } catch (SQLException e) {
        	 JOptionPane.showMessageDialog(frame, "Error adding order: " + e.getMessage());
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
    
    private void addSupplierProductGUI() {
        JComboBox<Integer> productComboBox = new JComboBox<>();
        JComboBox<Integer> orderComboBox = new JComboBox<>();
        JComboBox<Integer> orderCustomerCombox = new JComboBox<>();
        JTextField  Quantityew= new JTextField();

        // Populate the product combo box
        try {
            String sql = "SELECT ProudctId FROM proudct";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("ProudctId");
                productComboBox.addItem(productId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Populate the supplier combo box
        try {
            String sql = "SELECT OrderId FROM ordes";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                int OrderId = rs.getInt("OrderId");
                orderComboBox.addItem(OrderId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Populate the supplier combo box
        try {
            String sql = "SELECT Customer_CustomerId FROM ordes";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                int Customer_CustomerId = rs.getInt("Customer_CustomerId");
                orderCustomerCombox.addItem(Customer_CustomerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[] message = {
                "ProductId:", productComboBox,
                "OrderId:", orderComboBox,
                "CustuomerId:", orderCustomerCombox,
                "Quantity:", Quantityew
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Order-Product Relationship", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int productID = (int) productComboBox.getSelectedItem();
            int orderid = (int) orderComboBox.getSelectedItem();
            int orcustomerid = (int)orderCustomerCombox.getSelectedItem();
            String quantity=Quantityew.getText();
            addOrderProduct(productID,orderid,orcustomerid,quantity);
            JOptionPane.showMessageDialog(frame, "Order-Product added successfully.");
            
        }
    }

    public void removeOrderProduct(int productID, int orderID, int customerID) {
        try {
            String sql = "DELETE FROM `order contain proudct` WHERE Proudct_ProudctId=? AND Order_OrderId=? AND Order_Custumer_CustomerId=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, productID);
            pst.setInt(2, orderID);
            pst.setInt(3, customerID);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error removing order: " + ex.getMessage());
                }
            }
        }
    }
    

	private void removeSupplierProductGUI() {
	    JTextField productIDField = new JTextField();
	    JTextField orderIdfiled = new JTextField();
	    JTextField customerIddiled = new JTextField();

	    Object[] message = {
	            "Product ID:", productIDField,
	            "Order ID:", orderIdfiled,
	            "Order CustomerID:",customerIddiled
	            
	    };

	    int option = JOptionPane.showConfirmDialog(null, message, "Remove Order-Product Relationship", JOptionPane.OK_CANCEL_OPTION);
	    if (option == JOptionPane.OK_OPTION) {
	        int productID = Integer.parseInt(productIDField.getText());
	        int orderId = Integer.parseInt(orderIdfiled.getText());
	        int customerID = Integer.parseInt(customerIddiled.getText());
	        removeOrderProduct(productID,orderId,customerID);
	        
	        viewOrderProducts();
	       
	        
	       
	    }
	}

    public void viewOrderProducts() {
        try {
            String sql = "SELECT * FROM `order contain proudct`";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            // Clear existing rows in the table
            model.setRowCount(0);

            while (rs.next()) {
                int productID = rs.getInt("Proudct_ProudctId");
                int orderID = rs.getInt("Order_OrderId");
                int customerID = rs.getInt("Order_Custumer_CustomerId");
                String quantity = rs.getString("Quantity");
                Object[] row = {productID, orderID, customerID, quantity};
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error retrieving order products: " + ex.getMessage());
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error retrieving order products: " + ex.getMessage());
                }
            }
        }
    }
    
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new OrderProductManager();
            }
        });
    }
    
    
    
}
