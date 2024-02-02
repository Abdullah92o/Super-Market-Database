import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WarehouseIn {

    private JFrame frame;
    private DefaultTableModel model;
    private JTable warehouseTable;
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
                new WarehouseIn();
            }
        });
    }

    public WarehouseIn() {
        initialize();
        Connect();
        viewWarehouseTable();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 612, 447);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        

        JScrollPane scrollPane = new JScrollPane(warehouseTable);
        scrollPane.setBounds(10, 10, 580, 324);
        frame.getContentPane().add(scrollPane);

        JButton addButton = new JButton("Add Warehouse");
        addButton.setBounds(36, 361, 117, 29);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addWarehouse();
            }
        });
        frame.getContentPane().add(addButton);

        JButton deleteButton = new JButton("Delete Warehouse");
        deleteButton.setBounds(220, 361, 140, 29);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteWarehouse();
            }
        });
        frame.getContentPane().add(deleteButton);

        JButton viewButton = new JButton("View");
        viewButton.setBounds(429, 361, 117, 29);
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewWarehouseTable();
            }
        });
        frame.getContentPane().add(viewButton);
        
        
        model = new DefaultTableModel();
	    model.addColumn("Address");
	    model.addColumn("WarehouseName");
	    model.addColumn("Capacity");
	    
         
	    warehouseTable = new JTable(model);
	    scrollPane.setViewportView(warehouseTable);
	    // Create the table and set the model
	   

        frame.setVisible(true);
    }

    private void addWarehouse() {
    	 SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 new WarehouseForm();
             }
         });
    }

    private void deleteWarehouse() {
        String address = JOptionPane.showInputDialog(frame, "Enter the Warehouse Address to delete:");
        if (address != null && !address.isEmpty()) {
            try {
                String sql = "DELETE FROM warehouse WHERE Address = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, address);

                pst.executeUpdate();

                viewWarehouseTable();

                JOptionPane.showMessageDialog(frame, "Warehouse deleted successfully.");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Error deleting warehouse: " + e.getMessage());
            } finally {
                if (pst != null) {
                    try {
                        pst.close();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(frame, "Error deleting warehouse: " + e.getMessage());

                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid warehouse address. Please try again.");
        }
    }

    private void viewWarehouseTable() {
        DefaultTableModel model = (DefaultTableModel) warehouseTable.getModel();
        model.setRowCount(0);

        try {
            String sql = "SELECT * FROM warehouse";
            pst = con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String address = rs.getString("Address");
                String warehouseName = rs.getString("WarehouseName");
                int capacity = rs.getInt("Capacity");

                Object[] row = {address, warehouseName, capacity};
                model.addRow(row);
            }

        }catch (SQLException e) {
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
    }
