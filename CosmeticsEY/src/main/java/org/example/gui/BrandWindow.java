package org.example.gui;

import org.example.database.DatabaseConnection;
import org.example.service.BrandService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class BrandWindow {

    private JPanel brandPanel = new JPanel();
    private BrandService brandData = new BrandService();
    private JTable table;
    private DefaultTableModel model;

    public BrandWindow(JPanel mainPanel) throws SQLException {
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nameLabel = new JLabel("Brand name:");
        TextField nameTextField = new TextField(20);
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);
        brandPanel.add(namePanel);
        brandPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton edit = new JButton("Edit");
        buttonPanel.add(add);
        buttonPanel.add(delete);
        buttonPanel.add(edit);
        brandPanel.add(buttonPanel);
        brandPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel searchLabel = new JLabel("Search by:");
        TextField searchTextField = new TextField(20);
        JButton search = new JButton("Search");
        brandPanel.add(search);
        searchPanel.add(searchLabel);
        searchPanel.add(searchTextField);
        searchPanel.add(search);
        brandPanel.add(searchPanel);
        brandPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        createDataTable(brandPanel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int row = target.getSelectedRow();
                String selectedBrandName = target.getValueAt(row, 1).toString();
                try {
                    Map<Long, String> resultMap = brandData.filterBrandsByName(DatabaseConnection.getConnection(),selectedBrandName);
                    String name = resultMap.values().iterator().next();
                    nameTextField.setText(name);
                    nameTextField.repaint();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                try {
                    brandData.insertBrand(DatabaseConnection.getConnection(), name);
                    updateTable();
                    nameTextField.setText("");
                } catch (SQLException ex) {
                    System.out.println("Error inserting brand into the database: " + ex.getMessage());
                }
            }
        });


        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                try {
                    brandData.deleteBrandByName(DatabaseConnection.getConnection(), name);
                    nameTextField.setText("");
                    updateTable();
                } catch (SQLException ex) {
                    System.out.println("Error deleting brand from the database: " + ex.getMessage());
                }
            }
        });

        edit.addActionListener(new ActionListener() {
            String currentName = null;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentName == null) {
                    currentName = nameTextField.getText();
                    nameLabel.setText("New Name:");
                    edit.setText("Save changes");
                    nameTextField.setText("");
                } else {
                    String newName = nameTextField.getText();
                    try {
                            brandData.editBrand(DatabaseConnection.getConnection(), currentName, newName);
                            updateTable();
                            nameLabel.setText("Name:");
                            currentName = null;
                            nameTextField.setText("");
                            edit.setText("Edit");
                    } catch (SQLException ex) {
                        System.out.println("Error editing brand from the database: " + ex.getMessage());
                    }
                }
            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = searchTextField.getText();
                try {
                    Map<Long, String> resultMap = brandData.filterBrandsByName(DatabaseConnection.getConnection(), name);
                    printFoundBrands(resultMap);
                    searchTextField.setText("");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        brandPanel.setVisible(true);
        mainPanel.add(brandPanel);

    }


    public void createDataTable(JPanel mainPanel) throws SQLException {
        List<String> brandsNameList = brandData.getAllBrands(DatabaseConnection.getConnection());
        List<Integer> brandsIdList = brandData.getBrandIds(DatabaseConnection.getConnection());
        Map<Integer, String> brandsDataMap = new HashMap<>();

        for (int i = 0; i < brandsIdList.size(); i++) {
            brandsDataMap.put(brandsIdList.get(i), brandsNameList.get(i));
        }

        Object[][] tableData = new Object[brandsNameList.size()][2];
        int i = 0;
        for (Map.Entry<Integer, String> entry : brandsDataMap.entrySet()) {
            tableData[i][0] = entry.getKey();
            tableData[i][1] = entry.getValue();
            i++;
        }

        String[] columnNames = {"Brand ID", "Brand Name"};
        model = new DefaultTableModel(tableData, columnNames);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();


    }

    public void updateTable() throws SQLException {
        List<String> brandsNameList = brandData.getAllBrands(DatabaseConnection.getConnection());
        List<Integer> brandsIdList = brandData.getBrandIds(DatabaseConnection.getConnection());
        Map<Integer, String> brandsDataMap = new HashMap<>();

        for (int i = 0; i < brandsIdList.size(); i++) {
            brandsDataMap.put(brandsIdList.get(i), brandsNameList.get(i));
        }
        Map<Integer, String> sortedMap = new TreeMap<>(brandsDataMap);
        Object[][] newData = new Object[brandsNameList.size()][2];
        int i = 0;
        for (Map.Entry<Integer, String> entry : sortedMap.entrySet()) {
            newData[i][0] = entry.getKey();
            newData[i][1] = entry.getValue();
            i++;
        }


        String[] columnNames = {"Brand ID", "Brand Name"};

        model.setDataVector(newData, columnNames);
        table.revalidate();
        table.repaint();
    }

    public void printFoundBrands(Map<Long, String> map){

        Object[][] newData = new Object[1][2];
        int i = 0;
        for (Map.Entry<Long, String> entry : map.entrySet()) {
            newData[i][0] = entry.getKey();
            newData[i][1] = entry.getValue();
            i++;
        }

        String[] columnNames = {"Brand ID", "Brand Name"};

        model.setDataVector(newData, columnNames);
        table.revalidate();
        table.repaint();
    }


}