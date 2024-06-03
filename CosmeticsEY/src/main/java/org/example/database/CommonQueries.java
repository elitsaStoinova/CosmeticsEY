package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonQueries {
    public static boolean isTableEmpty(Connection connection, String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) AS rowcount FROM " + tableName;

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int rowCount = rs.getInt("rowcount");
                return rowCount == 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking if the table " + tableName + " is empty: " + e.getMessage());
        }
        return false;
    }

    public static void insertName(Connection connection, String name, String table) throws SQLException {
        String sql = "INSERT INTO " + table + " (name) VALUES (?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Value '" + name + "' was successfully added to the database.");
            } else {
                System.out.println("No maker was added to the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting into the database: " + e.getMessage());
        }
    }

    public static List<String> findAllFromDB(Connection connection, String database) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "SELECT name FROM " + database;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                result.add(name);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving all names: " + e.getMessage());
        }

        return result;
    }

    public static boolean deleteByName(Connection connection, String name, String table) throws SQLException {
        String sql = "DELETE FROM " + table + " WHERE name = ?";
        int affectedRows = 0;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);

            affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting by name: " + e.getMessage());
        }

        return affectedRows > 0;
    }

    public static boolean editName(Connection connection, String oldName, String newName, String table) throws SQLException {
        String sql = "UPDATE " + table + " SET name = ? WHERE name = ?";
        int affectedRows = 0;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, oldName);

            affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating by name: " + e.getMessage());
        }

        return affectedRows > 0;
    }

    public static List<Integer> getIds(Connection connection, String database) throws SQLException {
        List<Integer> result = new ArrayList<>();
        String sql = "SELECT id FROM " + database;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                result.add(id);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving ids: " + e.getMessage());
        }

        return result;
    }

    public static Map<Long, String> filterByName(Connection connection, String tableName, String nameByFilter){
        Map<Long, String> result = new HashMap<>();
        String sql = "SELECT id, name FROM " + tableName + " WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nameByFilter);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");

                result.put(id, name);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving ids: " + e.getMessage());
        }

        return result;
    }


}
