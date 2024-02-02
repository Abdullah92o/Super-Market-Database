import javax.swing.*;

import java.sql.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WarehouseForm {
    private JFrame frame;
    private JTextField addressField;
    private JTextField warehouseNameField;
    private JTextField capacityField;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

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

    public WarehouseForm() {
        connect();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Add Warehouse");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(337, 310);

        JPanel panel = new JPanel();

        addressField = new JTextField(10);
        addressField.setBounds(81, 5, 158, 20);
        warehouseNameField = new JTextField(10);
        warehouseNameField.setBounds(118, 41, 158, 20);
        capacityField = new JTextField(10);
        capacityField.setBounds(81, 84, 158, 20);
        panel.setLayout(null);

        JLabel label = new JLabel("Address:");
        label.setBounds(8, 8, 61, 14);
        panel.add(label);
        panel.add(addressField);
        JLabel label_1 = new JLabel("Warehouse Name:");
        label_1.setBounds(8, 44, 117, 14);
        panel.add(label_1);
        panel.add(warehouseNameField);
        JLabel label_2 = new JLabel("Capacity:");
        label_2.setBounds(8, 87, 61, 14);
        panel.add(label_2);
        panel.add(capacityField);

        JButton addButton = new JButton("Add Warehouse");
        addButton.setBounds(98, 142, 127, 23);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addWarehouse();
            }
        });

        panel.add(addButton);

        frame.getContentPane().add(panel);
        
        JButton CacelBTN = new JButton("Cancel");
        CacelBTN.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.dispose();
        	}
        });
        
        CacelBTN.setBounds(98, 189, 127, 23);
        panel.add(CacelBTN);
        frame.setVisible(true);
    }

    private void addWarehouse() {
        String address = addressField.getText();
        String warehouseName = warehouseNameField.getText();
        int capacity = Integer.parseInt(capacityField.getText());
        try {
            String sql = "INSERT INTO warehouse (Address, WarehouseName, Capacity) VALUES (?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, address);
            pst.setString(2, warehouseName);
            pst.setInt(3, capacity);

            pst.executeUpdate();

            
            JOptionPane.showMessageDialog(frame, "Warehouse added successfully.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error adding warehouse: " + e.getMessage());
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(frame, "Error adding warehouse: " + e.getMessage());
                }
            }

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WarehouseForm();
            }
        });
    }
}
