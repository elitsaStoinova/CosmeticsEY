package org.example.service;

import org.example.filter.ProductFilter;
import org.example.filter.ProductFilterFields;
import org.example.model.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductService {

    public void insertProduct(Connection connection, String name, double price, String description,
                              String makerName, String categoryName, Date bestBy) {
        String sql = "INSERT INTO products (name, price, description, maker_name, category_name, best_by) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setString(3, description);
            pstmt.setString(4, makerName);
            pstmt.setString(5, categoryName);
            if (bestBy == null) {
                pstmt.setNull(6, Types.DATE);
            } else {
                pstmt.setDate(6, bestBy);
            }
            pstmt.executeUpdate();
            System.out.println("Product '" + name + "' was successfully added to the database.");
        } catch (SQLException e) {
            System.out.println("Error inserting product into the database: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts(Connection connection) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, price, description, maker_name, category_name, best_by FROM products";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setDescription(rs.getString("description"));
                product.setMakerName(rs.getString("maker_name"));
                product.setCategoryName(rs.getString("category_name"));
                product.setBestBy(rs.getDate("best_by"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving all products: " + e.getMessage());
        }

        return products;
    }

    public boolean deleteProductByName(Connection connection, String productName){
        String sql = "DELETE FROM products WHERE name = ?";
        int affectedRows = 0;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting product by name: " + e.getMessage());
        }

        return affectedRows > 0;
    }

    public boolean editProduct(Connection connection, Product product, String currentProductName){
        String sql = "UPDATE products SET name = ?, price = ?, description = ?, maker_name = ?, category_name = ?, best_by = ? WHERE id = ?";
        boolean isUpdated = false;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getDescription());
            pstmt.setString(4, product.getMakerName());
            pstmt.setString(5, product.getCategoryName());
            pstmt.setDate(6, product.getBestBy());
            pstmt.setLong(7, product.getId());

            int affectedRows = pstmt.executeUpdate();
            isUpdated = affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }

        return isUpdated;
    }

    public Product findProductByName(Connection connection, String productName) {
        String sql = "SELECT id, name, price, description, maker_name, category_name, best_by FROM products WHERE name = ?";
        Product product = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setDescription(rs.getString("description"));
                product.setMakerName(rs.getString("maker_name"));
                product.setCategoryName(rs.getString("category_name"));
                product.setBestBy(rs.getDate("best_by"));
            }
        } catch (SQLException e) {
            System.out.println("Error finding product by name: " + e.getMessage());
        }

        return product;
    }

    public List<Product> filterProducts(Connection connection, ProductFilter filter) throws SQLException {
        List<Product> allProducts = this.getAllProducts(connection);
        Predicate<Product> predicate = ProductFilterFields.buildPredicate(filter);

        return allProducts.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

}
