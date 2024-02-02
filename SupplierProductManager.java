import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SupplierProductManager {
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private JFrame frame;
    private JTable supplierProductTable;
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
    public SupplierProductManager() {
    	connect();
    	initializeGUI();
    	viewSupplierProducts();
    	
    }
    

    public void disconnect() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
                System.out.println("Disconnected from MySQL database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addSupplierProduct(int productID, int supplierID, float unitCost) {
        try {
            String sql = "INSERT INTO `supplier supplies proudct` (Proudct_ProudctId , Supplier_SupplierId , UnitCost) VALUES (?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, productID);
            pst.setInt(2, supplierID);
            pst.setFloat(3, unitCost);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                	 JOptionPane.showMessageDialog(frame, "Error adding order: " + ex.getMessage());
                }
            }
        }
    }

    public void removeSupplierProduct(int productID, int supplierID) {
        try {
            String sql = "DELETE FROM `supplier supplies proudct` WHERE Proudct_ProudctId=? AND Supplier_SupplierId=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, productID);
            pst.setInt(2, supplierID);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                	 JOptionPane.showMessageDialog(frame, "Error adding order: " + ex.getMessage());
                }
            }
        }
    }

    public void viewSupplierProducts() {
        try {
            String sql = "SELECT * FROM `supplier supplies proudct`";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            // Clear existing rows in the table
            model.setRowCount(0);

            while (rs.next()) {
                int productID = rs.getInt("Proudct_ProudctId");
                int supplierID = rs.getInt("Supplier_SupplierId");
                float unitCost = rs.getFloat("UnitCost");
                Object[] row = {productID, supplierID, unitCost};
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                	 JOptionPane.showMessageDialog(frame, "Error adding order: " + ex.getMessage());
                }
            }
        }
    }

    public void initializeGUI() {
        frame = new JFrame("Supplier-Product Manager");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 464, 300);
        frame.getContentPane().add(scrollPane);

        model = new DefaultTableModel();
        model.addColumn("ProductID");
        model.addColumn("SupplierID");
        model.addColumn("UnitCost");

        supplierProductTable = new JTable(model);
        scrollPane.setViewportView(supplierProductTable);

        JButton addButton = new JButton("Add");
        addButton.setBounds(10, 320, 100, 30);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSupplierProductGUI();
            }

			
        });
        frame.getContentPane().add(addButton);

        JButton removeButton = new JButton("Remove");
        removeButton.setBounds(120, 320, 100, 30);
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSupplierProductGUI();
            }
        });
        frame.getContentPane().add(removeButton);

        JButton viewButton = new JButton("View");
        viewButton.setBounds(230, 320, 100, 30);
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewSupplierProducts();
            }
        });
        frame.getContentPane().add(viewButton);

        frame.setVisible(true);
    }
    
    
 

    private void addSupplierProductGUI() {
        JComboBox<Integer> productComboBox = new JComboBox<>();
        JComboBox<Integer> supplierComboBox = new JComboBox<>();
        JTextField unitCostField = new JTextField();

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
            String sql = "SELECT SupplierId FROM supplier";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                int supplierId = rs.getInt("SupplierId");
                supplierComboBox.addItem(supplierId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[] message = {
                "Product:", productComboBox,
                "Supplier:", supplierComboBox,
                "Unit Cost:", unitCostField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Supplier-Product Relationship", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int productID = (int) productComboBox.getSelectedItem();
            int supplierID = (int) supplierComboBox.getSelectedItem();
            float unitCost = Float.parseFloat(unitCostField.getText());
            addSupplierProduct(productID, supplierID, unitCost);
            JOptionPane.showMessageDialog(frame, "Supplier-Product added successfully.");
            viewSupplierProducts();
        }
    }

		private void removeSupplierProductGUI() {
		    JTextField productIDField = new JTextField();
		    JTextField supplierIDField = new JTextField();

		    Object[] message = {
		            "Product ID:", productIDField,
		            "Supplier ID:", supplierIDField
		    };

		    int option = JOptionPane.showConfirmDialog(null, message, "Remove Supplier-Product Relationship", JOptionPane.OK_CANCEL_OPTION);
		    if (option == JOptionPane.OK_OPTION) {
		        int productID = Integer.parseInt(productIDField.getText());
		        int supplierID = Integer.parseInt(supplierIDField.getText());
		        removeSupplierProduct(productID, supplierID);
		        viewSupplierProducts();
		    }
		}
	
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SupplierProductManager();
            }
        });
    }
    
    
    
    
    
}
