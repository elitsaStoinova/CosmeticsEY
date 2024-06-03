package org.example.filter;

import org.example.model.entity.Product;

import java.util.function.Predicate;

public class ProductFilterFields {

    public static Predicate<Product> buildPredicate(ProductFilter filter) {
        Predicate<Product> predicate = p -> true;

        if (filter.getName() != null) {
            predicate = predicate.and(p -> p.getName().equals(filter.getName()));
        }
        if (filter.getPrice() != null) {
            predicate = predicate.and(p -> p.getPrice().compareTo(filter.getPrice()) == 0);
        }
        if (filter.getDescription() != null) {
            predicate = predicate.and(p -> p.getDescription().equals(filter.getDescription()));
        }
        if (filter.getBrandName() != null && !filter.getBrandName().equals("Choose brand")) {
            predicate = predicate.and(p -> p.getMakerName().equals(filter.getBrandName()));
        }
        if (filter.getCategoryName() != null && !filter.getCategoryName().equals("Choose category")) {
            predicate = predicate.and(p -> p.getCategoryName().equals(filter.getCategoryName()));
        }
        if (filter.getBestBy() != null) {
            predicate = predicate.and(p -> p.getBestBy().equals(filter.getBestBy()));
        }

        return predicate;
    }

}
