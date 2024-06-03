package org.example.service;

import org.example.database.CommonQueries;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BrandService {

    private static final String TABLE_NAME = "makers";

    public void insertBrand(Connection connection, String brandName) throws SQLException {
        CommonQueries.insertName(connection, brandName, TABLE_NAME);
    }

    public boolean deleteBrandByName(Connection connection, String name) throws SQLException {
        return CommonQueries.deleteByName(connection, name, TABLE_NAME);
    }

    public boolean editBrand(Connection connection, String oldName, String newName) throws SQLException {
        return CommonQueries.editName(connection, oldName, newName, TABLE_NAME);
    }

    public List<String> getAllBrands(Connection connection) throws SQLException {
        return CommonQueries.findAllFromDB(connection, TABLE_NAME);
    }

    public List<Integer> getBrandIds(Connection connection) throws SQLException {
        return CommonQueries.getIds(connection, TABLE_NAME);
    }

    public Map<Long, String> filterBrandsByName(Connection connection, String brandName){
        return CommonQueries.filterByName(connection, TABLE_NAME, brandName);
    }


}
