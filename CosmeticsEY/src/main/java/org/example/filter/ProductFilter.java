package org.example.filter;

import java.sql.Date;

public class ProductFilter {
    private String name;
    private Double price;
    private String description;
    private String brandName;
    private String categoryName;
    private Date bestBy;

    public ProductFilter() {
    }

    public String getName() {
        return name;
    }

    public ProductFilter setName(String name) {
        this.name = name;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public ProductFilter setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductFilter setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getBrandName() {
        return brandName;
    }

    public ProductFilter setBrandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public ProductFilter setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public Date getBestBy() {
        return bestBy;
    }

    public ProductFilter setBestBy(Date bestBy) {
        this.bestBy = bestBy;
        return this;
    }

}
