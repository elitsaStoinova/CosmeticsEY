package org.example.service;

import org.example.database.CommonQueries;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CategoryService {

    private final String TABLE_NAME = "categories";

    public void insertCategory(Connection connection, String categoryName) throws SQLException {
        CommonQueries.insertName(connection, categoryName, TABLE_NAME);
    }

    public List<String> getAllCategories(Connection connection) throws SQLException {
        return CommonQueries.findAllFromDB(connection, TABLE_NAME);
    }

    public boolean deleteCategory(Connection connection, String categoryName) throws SQLException {
        return CommonQueries.deleteByName(connection, categoryName, TABLE_NAME);
    }

    public boolean editCategoryByName(Connection connection, String oldName, String newName) throws SQLException {
        return CommonQueries.editName(connection, oldName, newName, TABLE_NAME);
    }

    public List<Integer> getCategoryIds(Connection connection) throws SQLException {
        return CommonQueries.getIds(connection, TABLE_NAME);
    }

    public Map<Long, String> filterCategoriesByName(Connection connection, String categoryName){
        return CommonQueries.filterByName(connection, TABLE_NAME, categoryName);
    }

}
