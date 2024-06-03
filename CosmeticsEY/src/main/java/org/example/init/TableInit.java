package org.example.init;

import org.example.database.DatabaseConnection;

import java.sql.*;

public class TableInit {

    public static void checkForTablesExistence() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {

            if (!checkTableExists(connection, "makers")) {
                createTableBrands(connection);
                System.out.println("Table 'brands' created successfully.");
            }

            if (!checkTableExists(connection, "categories")) {
                createTableCategories(connection);
                System.out.println("Table 'categories' created successfully.");
            }

            if (!checkTableExists(connection, "products")) {
                createTableProducts(connection);
                System.out.println("Table 'products' created successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void createTableBrands(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE makers (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "PRIMARY KEY (id), " +
                    "UNIQUE (name))";
            stmt.executeUpdate(sql);
        }
    }

    private static void createTableCategories(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE categories (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "PRIMARY KEY (id), " +
                    "UNIQUE (name))";
            stmt.executeUpdate(sql);
        }
    }

    private static void createTableProducts(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE products (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "price DECIMAL(10, 2) NOT NULL, " +
                    "description TEXT, " +
                    "maker_name VARCHAR(255) NOT NULL, " +
                    "category_name VARCHAR(255) NOT NULL, " +
                    "best_by DATE, " +
                    "PRIMARY KEY (id), " +
                    "UNIQUE (name), " +
                    "FOREIGN KEY (maker_name) REFERENCES makers(name) ON UPDATE CASCADE ON DELETE CASCADE, " +
                    "FOREIGN KEY (category_name) REFERENCES categories(name) ON UPDATE CASCADE ON DELETE CASCADE)";
            stmt.executeUpdate(sql);
        }
    }

    private static boolean checkTableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        try (ResultSet resultSet = databaseMetaData
                .getTables("cosmetics", null, tableName, new String[]{"TABLE"})) {
            while (resultSet.next()) {
                String name = resultSet.getString("TABLE_NAME");
                System.out.println("Found table: " + name + " in cosmetics database");
                if (name.equalsIgnoreCase(tableName)) {
                    return true;
                }
            }
        }
        return false;
    }
}