package org.example.init;

import org.example.database.CommonQueries;
import org.example.database.DatabaseConnection;
import org.example.service.CategoryService;
import org.example.service.BrandService;
import org.example.service.ProductService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class DataInit {

    private CategoryService categoryService = new CategoryService();
    private BrandService brandService = new BrandService();
    private ProductService productService = new ProductService();

    public void initSampleData() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();

        if (CommonQueries.isTableEmpty(connection, "categories")) {
            categoryService.insertCategory(connection, "Skincare");
            categoryService.insertCategory(connection, "Makeup");
            categoryService.insertCategory(connection, "Haircare");
            categoryService.insertCategory(connection, "Body-care");
            categoryService.insertCategory(connection, "Professional Beauty");
            categoryService.insertCategory(connection, "Wellness");
        }

        if (CommonQueries.isTableEmpty(connection, "makers")) {
            brandService.insertBrand(connection, "BabyLiss");
            brandService.insertBrand(connection, "La Roche");
            brandService.insertBrand(connection, "Eucerin");
            brandService.insertBrand(connection, "Avene");
            brandService.insertBrand(connection, "Fortex");
        }

        if (CommonQueries.isTableEmpty(connection, "products")) {
            productService.insertProduct(connection, "Product Name", 20.00, "Descr",
                    "La Roche", "Wellness", Date.valueOf(LocalDate.now()));

            productService.insertProduct(connection, "Product Name2", 10.00, "Descr",
                    "La Roche", "Haircare", Date.valueOf(LocalDate.now()));

        }

    }
}
