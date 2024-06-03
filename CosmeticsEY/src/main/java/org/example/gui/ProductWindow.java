package org.example.gui;

import org.example.database.DatabaseConnection;
import org.example.model.entity.Product;
import org.example.service.ProductService;
import org.example.service.BrandService;
import org.example.service.CategoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class ProductWindow {
    private JPanel productPanel = new JPanel();
    private ProductService productData = new ProductService();
    private BrandService brandsData =new BrandService();
    private CategoryService categoryData =new CategoryService();

    private JTable table;
    private DefaultTableModel model;

    public ProductWindow(JPanel mainPanel) throws SQLException {
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,60,0));
        JLabel nameLabel = new JLabel("Product name:");
        Dimension labelSize = new Dimension(100, nameLabel.getPreferredSize().height);
        nameLabel.setPreferredSize(labelSize);
        TextField nameTextField = new TextField(17);
        namePanel.add(nameLabel,BorderLayout.WEST);
        namePanel.add(nameTextField,BorderLayout.EAST);
        productPanel.add(namePanel);
        productPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,60,0));
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setPreferredSize(labelSize);
        TextField priceTextField = new TextField(17);
        pricePanel.add(priceLabel,BorderLayout.WEST);
        pricePanel.add(priceTextField,BorderLayout.EAST);
        productPanel.add(pricePanel);
        productPanel.add(Box.createRigidArea(new Dimension(0, 5)));


        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,60,0));
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setPreferredSize(labelSize);
        TextField descriptionTextField = new TextField(17);
        descriptionPanel.add(descriptionLabel,BorderLayout.WEST);
        descriptionPanel.add(descriptionTextField,BorderLayout.EAST);
        productPanel.add(descriptionPanel);
        productPanel.add(Box.createRigidArea(new Dimension(0, 5)));


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
        productPanel.add(brandPanel);
        productPanel.add(Box.createRigidArea(new Dimension(0, 5)));


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
        productPanel.add(categoryPanel);
        productPanel.add(Box.createRigidArea(new Dimension(0, 5)));


        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,60,0));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setPreferredSize(labelSize);
        JFormattedTextField dateTextField = new JFormattedTextField(dateFormatter);
        dateTextField.setPreferredSize(new Dimension(160, 20));
        dateTextField.setValue(new java.util.Date(System.currentTimeMillis()));
        datePanel.add(dateLabel,BorderLayout.WEST);
        datePanel.add(dateTextField);
        productPanel.add(datePanel);
        productPanel.add(Box.createRigidArea(new Dimension(0, 5)));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton edit = new JButton("Change");
        buttonPanel.add(add);
        buttonPanel.add(delete);
        buttonPanel.add(edit);
        productPanel.add(buttonPanel);
        productPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel searchLabel = new JLabel("Search by name:");
        TextField searchTextField = new TextField(20);
        JButton search = new JButton("Search");
        searchPanel.add(searchLabel);
        searchPanel.add(searchTextField);
        searchPanel.add(search);
        productPanel.add(searchPanel);
        productPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        createDataTable(productPanel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int row = target.getSelectedRow();
                String selectedProductName = target.getValueAt(row, 1).toString();
                try {
                    Product selectedProduct = productData.findProductByName(DatabaseConnection.getConnection(),selectedProductName);
                    String name = selectedProduct.getName();
                    nameTextField.setText(name);
                    nameTextField.repaint();
                    priceTextField.setText(selectedProduct.getPrice().toString());
                    priceTextField.repaint();
                    descriptionTextField.setText(selectedProduct.getDescription());
                    descriptionTextField.repaint();
                    dateTextField.setValue(selectedProduct.getBestBy());
                    dateTextField.repaint();
                    comboBoxCategory.setSelectedItem(selectedProduct.getCategoryName());
                    comboBoxCategory.repaint();
                    comboBoxBrand.setSelectedItem(selectedProduct.getMakerName());
                    comboBoxBrand.repaint();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                Double price = Double.parseDouble(priceTextField.getText());
                String description = descriptionTextField.getText();
                String brandName = (String) comboBoxBrand.getSelectedItem();
                String categoryName = (String) comboBoxCategory.getSelectedItem();
                Date utilDate = (Date) dateTextField.getValue();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                try {
                    productData.insertProduct(DatabaseConnection.getConnection(), name,price,description,brandName,categoryName,sqlDate);
                    updateTable();
                    nameTextField.setText("");
                    priceTextField.setText("");
                    descriptionTextField.setText("");
                } catch (SQLException ex) {
                    System.out.println("Error inserting product into the database: " + ex.getMessage());
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                try {
                    productData.deleteProductByName(DatabaseConnection.getConnection(), name);
                    nameTextField.setText("");
                    priceTextField.setText("");
                    descriptionTextField.setText("");
                    updateTable();
                } catch (SQLException ex) {
                    System.out.println("Error deleting product from the database: " + ex.getMessage());
                }
            }
        });

        edit.addActionListener(new ActionListener() {
            String currentName = null;
            @Override
            public void actionPerformed(ActionEvent e) {
                Product currentProduct = new Product();
                if (currentName == null) {
                    currentName = nameTextField.getText();
                    try {
                        currentProduct = productData.findProductByName(DatabaseConnection.getConnection(),currentName);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    edit.setText("Save changes");
                    priceTextField.setText(currentProduct.getPrice().toString());
                    descriptionTextField.setText(currentProduct.getDescription());
                    comboBoxBrand.setSelectedItem(currentProduct.getMakerName());
                    comboBoxCategory.setSelectedItem(currentProduct.getCategoryName());
                    dateTextField.setValue(currentProduct.getBestBy());
                } else {
                    try {
                        currentProduct = productData.findProductByName(DatabaseConnection.getConnection(),currentName);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    String newName = nameTextField.getText();
                    Double newPrice = Double.parseDouble(priceTextField.getText());
                    String newDescription = descriptionTextField.getText();
                    String newBrandName = (String) comboBoxBrand.getSelectedItem();
                    String newCategoryName = (String) comboBoxCategory.getSelectedItem();
                    Date utilDate = (Date) dateTextField.getValue();
                    java.sql.Date newSqlDate = new java.sql.Date(utilDate.getTime());
                    Product newProduct = new Product(currentProduct.getId(),newName,newPrice,newDescription,newBrandName,newCategoryName,newSqlDate);
                    try {
                        productData.editProduct(DatabaseConnection.getConnection(), newProduct, currentProduct.getName());
                        updateTable();
                        nameLabel.setText("Name:");
                        currentName = null;
                        nameTextField.setText("");
                        priceTextField.setText("");
                        descriptionTextField.setText("");
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
                    Product foundProduct = productData.findProductByName(DatabaseConnection.getConnection(),name);
                    printFoundProduct(foundProduct);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        productPanel.setVisible(true);
        mainPanel.add(productPanel);
    }

    public void createDataTable(JPanel mainPanel) throws SQLException {
        List<Product> productsList = productData.getAllProducts(DatabaseConnection.getConnection());

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
        model = new DefaultTableModel(tableData, columnNames);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void updateTable() throws SQLException {
        List<Product> productsList = productData.getAllProducts(DatabaseConnection.getConnection());


        Object[][] newData = new Object[productsList.size()][7];
        for(int i=0;i<productsList.size();i++){
            newData[i][0] = productsList.get(i).getId();
            newData[i][1] = productsList.get(i).getName();
            newData[i][2] = productsList.get(i).getPrice();
            newData[i][3] = productsList.get(i).getDescription();
            newData[i][4] = productsList.get(i).getMakerName();
            newData[i][5] = productsList.get(i).getCategoryName();
            newData[i][6] = productsList.get(i).getBestBy();

        }



        String[] columnNames = {"Product ID", "Name" , "Price", "Description", "Brand", "Category", "Date"};

        model.setDataVector(newData, columnNames);
        table.revalidate();
        table.repaint();
    }

    public void printFoundProduct(Product product){
        Object[][] newData = new Object[1][7];

        for(int i=0;i<1;i++){
            newData[i][0] = product.getId();
            newData[i][1] = product.getName();
            newData[i][2] = product.getPrice();
            newData[i][3] = product.getDescription();
            newData[i][4] = product.getMakerName();
            newData[i][5] = product.getCategoryName();
            newData[i][6] = product.getBestBy();
        }

        String[] columnNames = {"Product ID", "Name" , "Price", "Description", "Brand", "Category", "Date"};

        model.setDataVector(newData, columnNames);
        table.revalidate();
        table.repaint();
    }

}
