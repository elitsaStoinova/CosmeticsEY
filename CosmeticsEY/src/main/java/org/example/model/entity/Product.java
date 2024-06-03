package org.example.model.entity;

import java.sql.Date;

public class Product {
    private long id;
    private String name;
    private Double price;
    private String description;
    private String makerName;
    private String categoryName;
    private Date bestBy;

    public Product() {
    }
    public Product(long id, String name, Double price, String description,
                   String makerName, String categoryName, Date bestBy) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.makerName = makerName;
        this.categoryName = categoryName;
        this.bestBy = bestBy;
    }

    public long getId() {
        return id;
    }

    public Product setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Product setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getMakerName() {
        return makerName;
    }

    public Product setMakerName(String makerName) {
        this.makerName = makerName;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Product setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public Date getBestBy() {
        return bestBy;
    }

    public Product setBestBy(Date bestBy) {
        this.bestBy = bestBy;
        return this;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", makerName='" + makerName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", bestBy=" + bestBy +
                '}';
    }

}
