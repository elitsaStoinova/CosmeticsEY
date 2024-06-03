package org.example.gui;

import org.example.database.DatabaseConnection;
import org.example.filter.ProductFilter;
import org.example.filter.ProductFilterFields;
import org.example.model.entity.Product;
import org.example.service.BrandService;
import org.example.service.CategoryService;
import org.example.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public class FilterWindow {
    private JPanel filterPanel = new JPanel();
    private ProductService productService = new ProductService();
    private BrandService brandsData = new BrandService();
    private CategoryService categoryData = new CategoryService();
    private JTable table;
    private DefaultTableModel model;

    public FilterWindow(JPanel mainPanel) throws SQLException {
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 60,0));
        JLabel nameLabel = new JLabel("Product name:");
        Dimension labelSize = new Dimension(100, nameLabel.getPreferredSize().height);
        nameLabel.setPreferredSize(labelSize);
        TextField nameTextField = new TextField(17);
        namePanel.add(nameLabel,BorderLayout.WEST);
        namePanel.add(nameTextField,BorderLayout.EAST);
        filterPanel.add(namePanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));


        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,60,0));
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setPreferredSize(labelSize);
        TextField priceTextField = new TextField(17);
        pricePanel.add(priceLabel, BorderLayout.WEST);
        pricePanel.add(priceTextField, BorderLayout.EAST);
        filterPanel.add(pricePanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));


        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,60,0));
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setPreferredSize(labelSize);
        TextField descriptionTextField = new TextField(17);
        descriptionPanel.add(descriptionLabel,BorderLayout.WEST);
        descriptionPanel.add(descriptionTextField,BorderLayout.EAST);
        filterPanel.add(descriptionPanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));


        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,60,0));
        JLabel brandNameLabel = new JLabel("Brand name:");
        brandNameLabel.setPreferredSize(labelSize);
        List<String> brandItems = brandsData.getAllBrands(DatabaseConnection.getConnection());
        brandItems.add(0, "Choose brand");
        String[] brandItemsArray = brandItems.toArray(new String[0]);
        JComboBox<String> comboBoxBrand = new JComboBox<>(brandItemsArray);
        comboBoxBrand.setPreferredSize(new Dimension(160,20));
        brandPanel.add(brandNameLabel,BorderLayout.WEST);
        brandPanel.add(comboBoxBrand,BorderLayout.EAST);
        filterPanel.add(brandPanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));



        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,60,0));
        JLabel categoryNameList = new JLabel("Category:");
        categoryNameList.setPreferredSize(labelSize);
        List<String> categoryItems = categoryData.getAllCategories(DatabaseConnection.getConnection());
        categoryItems.add(0, "Choose category");
        String[] categoryItemsArray = categoryItems.toArray(new String[0]);
        JComboBox<String> comboBoxCategory = new JComboBox<>(categoryItemsArray);
        comboBoxCategory.setPreferredSize(new Dimension(160,20));
        categoryPanel.add(categoryNameList,BorderLayout.WEST);
        categoryPanel.add(comboBoxCategory);
        filterPanel.add(categoryPanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,60,0));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setPreferredSize(labelSize);
        JFormattedTextField dateTextField = new JFormattedTextField(dateFormatter);
        dateTextField.setPreferredSize(new Dimension(160, 20));
//        dateTextField.setValue(new Date(System.currentTimeMillis()));
        datePanel.add(dateLabel,BorderLayout.WEST);
        datePanel.add(dateTextField);
        filterPanel.add(datePanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton search = new JButton("Search");
        buttonPanel.add(search);
        filterPanel.add(buttonPanel);
        printTable();


        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductFilter productFilter = new ProductFilter();
                if (null != nameTextField.getText() && !nameTextField.getText().isEmpty()) {
                    productFilter.setName(nameTextField.getText());
                }

                if (null != priceTextField.getText() && !priceTextField.getText().isEmpty()) {
                    productFilter.setPrice(Double.valueOf(priceTextField.getText()));
                }

                if (null != descriptionTextField.getText() && !descriptionTextField.getText().isEmpty()) {
                    productFilter.setName(descriptionTextField.getText());
                }

                if (null != comboBoxCategory.getSelectedItem().toString()
                        && !comboBoxCategory.getSelectedItem().toString().isEmpty()
                        && !comboBoxCategory.getSelectedItem().toString().equals("Choose category")) {
                    productFilter.setCategoryName(comboBoxCategory.getSelectedItem().toString());
                }

                if (null != comboBoxBrand.getSelectedItem().toString()
                        && !comboBoxBrand.getSelectedItem().toString().isEmpty()
                        && !comboBoxCategory.getSelectedItem().toString().equals("Choose brand")) {
                    productFilter.setBrandName(comboBoxBrand.getSelectedItem().toString());
                }

                if (null == dateTextField.getText() && dateTextField.getText().isEmpty()) {
                    productFilter.setBestBy(Date.valueOf(dateTextField.getText()));
                }

                List<Product> products = new ArrayList<>();
                try {
                    products = productService.filterProducts(DatabaseConnection.getConnection(), productFilter);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                if (products.size() == 0){
                    JLabel noProductFoundMessage = new JLabel("No products found !");
                    filterPanel.add(noProductFoundMessage);
                }else {
                    printFoundProduct(products);
                }

                nameTextField.setText("");
                priceTextField.setText("");
                descriptionTextField.setText("");
                comboBoxBrand.setSelectedIndex(0);
                comboBoxCategory.setSelectedIndex(0);
                dateTextField.setText("");
            }
        });

        filterPanel.setVisible(true);
        mainPanel.add(filterPanel);
    }


    public void printTable(){
        Object[][] tableData = new Object[1][7];
        String[] columnNames = {"Product ID", "Name" , "Price", "Description", "Brand", "Category", "Date"};
        model = new DefaultTableModel(tableData, columnNames);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        filterPanel.add(scrollPane, BorderLayout.CENTER);
        filterPanel.revalidate();
        filterPanel.repaint();
    }

    public void printFoundProduct(List<Product> productsList) {

        Object[][] tableData = new Object[productsList.size()][7];
        for(int i=0;i<productsList.size();i++){
            tableData[i][0] = productsList.get(i).getId();
            tableData[i][1] = productsList.get(i).getName();
            tableData[i][2] = productsList.get(i).getPrice();
            tableData[i][3] = productsList.get(i).getDescription();
            tableData[i][4] = productsList.get(i).getMakerName();
            tableData[i][5] = productsList.get(i).getCategoryName();
            tableData[i][6] = productsList.get(i).getBestBy();

        }

        String[] columnNames = {"Product ID", "Name" , "Price", "Description", "Brand", "Category", "Date"};

        model.setDataVector(tableData, columnNames);
        table.revalidate();
        table.repaint();
    }
}
