package org.example.gui;

import org.example.database.DatabaseConnection;
import org.example.service.CategoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CategoryWindow {

    private JPanel categoryPanel = new JPanel();
    private CategoryService categoryData = new CategoryService();
    private JTable table;
    private DefaultTableModel model;


    public CategoryWindow(JPanel mainPanel) throws SQLException {
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nameLabel = new JLabel("Category name:");
        TextField nameTextField = new TextField(20);
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);
        categoryPanel.add(namePanel);
        categoryPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton edit = new JButton("Edit");
        buttonPanel.add(add);
        buttonPanel.add(delete);
        buttonPanel.add(edit);
        categoryPanel.add(buttonPanel);
        categoryPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel searchLabel = new JLabel("Search by:");
        TextField searchTextField = new TextField(20);
        JButton search = new JButton("Search");
        categoryPanel.add(search);
        searchPanel.add(searchLabel);
        searchPanel.add(searchTextField);
        searchPanel.add(search);
        categoryPanel.add(searchPanel);
        categoryPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        createDataTable(categoryPanel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int row = target.getSelectedRow();
                String selectedBrandName = target.getValueAt(row, 1).toString();
                try {
                    Map<Long, String> resultMap = categoryData.filterCategoriesByName(DatabaseConnection.getConnection(),selectedBrandName);
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
                    categoryData.insertCategory(DatabaseConnection.getConnection(), name);
                    updateTable();
                    nameTextField.setText("");
                } catch (SQLException ex) {
                    System.out.println("Error inserting category into the database: " + ex.getMessage());
                }
            }
        });


        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                try {
                    categoryData.deleteCategory(DatabaseConnection.getConnection(), name);
                    nameTextField.setText("");
                    updateTable();
                } catch (SQLException ex) {
                    System.out.println("Error deleting category from the database: " + ex.getMessage());
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
                        categoryData.editCategoryByName(DatabaseConnection.getConnection(), currentName, newName);
                        updateTable();
                        nameLabel.setText("Name:");
                        currentName = null;
                        nameTextField.setText("");
                        edit.setText("Edit");
                    } catch (SQLException ex) {
                        System.out.println("Error editing category from the database: " + ex.getMessage());
                    }
                }
            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = searchTextField.getText();
                try {
                    Map<Long, String> resultMap = categoryData.filterCategoriesByName(DatabaseConnection.getConnection(), name);
                    printFoundProduct(resultMap);
                    searchTextField.setText("");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        categoryPanel.setVisible(true);
        mainPanel.add(categoryPanel);
    }


    public void createDataTable(JPanel mainPanel) throws SQLException {
        List<String> categiryNameList = categoryData.getAllCategories(DatabaseConnection.getConnection());
        List<Integer> categoryIdList = categoryData.getCategoryIds(DatabaseConnection.getConnection());
        Map<Integer, String> categoryDataMap = new HashMap<>();

        for (int i = 0; i < categoryIdList.size(); i++) {
            categoryDataMap.put(categoryIdList.get(i), categiryNameList.get(i));
        }

        Object[][] tableData = new Object[categiryNameList.size()][2];
        int i = 0;
        for (Map.Entry<Integer, String> entry : categoryDataMap.entrySet()) {
            tableData[i][0] = entry.getKey();
            tableData[i][1] = entry.getValue();
            i++;
        }

        String[] columnNames = {"Category ID", "Category Name"};
        model = new DefaultTableModel(tableData, columnNames);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void updateTable() throws SQLException {
        List<String> categoryNameList = categoryData.getAllCategories(DatabaseConnection.getConnection());
        List<Integer> categoryIdList = categoryData.getCategoryIds(DatabaseConnection.getConnection());
        Map<Integer, String> categoryDataMap = new HashMap<>();

        for (int i = 0; i < categoryIdList.size(); i++) {
            categoryDataMap.put(categoryIdList.get(i), categoryNameList.get(i));
        }
        Map<Integer, String> sortedMap = new TreeMap<>(categoryDataMap);
        Object[][] newData = new Object[categoryNameList.size()][2];
        int i = 0;
        for (Map.Entry<Integer, String> entry : sortedMap.entrySet()) {
            newData[i][0] = entry.getKey();
            newData[i][1] = entry.getValue();
            i++;
        }


        String[] columnNames = {"Category ID", "Category Name"};

        model.setDataVector(newData, columnNames);
        table.revalidate();
        table.repaint();
    }

    public void printFoundProduct(Map<Long, String> map){

        Object[][] newData = new Object[1][2];
        int i = 0;
        for (Map.Entry<Long, String> entry : map.entrySet()) {
            newData[i][0] = entry.getKey();
            newData[i][1] = entry.getValue();
            i++;
        }


        String[] columnNames = {"Category ID", "Category Name"};

        model.setDataVector(newData, columnNames);
        table.revalidate();
        table.repaint();
    }
}
