package com.skapica.service;

import com.skapica.model.Product;
import com.skapica.model.ProductType;

import java.util.List;
import java.util.stream.Collectors;

public class ProductValidator {

    /**
     * Validates if extra ingredient can be added to product
     * @param product product to validate
     * @throws IllegalArgumentException when adding extras to product is not allowed
     */
    public static void validateIfExtrasCanBeAdded(Product product) {
        if (!product.getName().toLowerCase().contains("coffee")){
            throw new IllegalArgumentException("Extras can be ordered only with coffee");
        }
    }

    /**
     * Validates if product for given name exists and if is unique
     * @param productName name of product to validate
     * @param matchingProducts list of {@link Product} to verify
     * @throws IllegalArgumentException when no product found or collection contains more than one item
     *
     */
    public static void validateIfProductExistsAndIsUnique(String productName, List<Product> matchingProducts) {
        if (matchingProducts.isEmpty())
            throw new IllegalArgumentException("No product found: " + productName);
        else if (matchingProducts.size() > 1)
            throw new IllegalArgumentException("Product name ambiguous. Specify your order. Found: " + matchingProducts.stream().map(Product::getName).collect(Collectors.joining(", ")));
    }

    /**
     * Validates if product can be sold.
     * Extra products cannot be sold separately.
     * Main products cannot be addition to another main product.
     *
     * @param findPrimary - determines if expected product type is primary product
     * @param product product to check
     * @throws IllegalArgumentException when validation fails.
     */
    public static void validateIfProductCanBeSold(boolean findPrimary, Product product) {
        if (ProductType.EXTRA.equals(product.getType()) && findPrimary)
            throw new IllegalArgumentException("Extras cannot be ordered separately: invalid product: " + product.getName());
        if (!ProductType.EXTRA.equals(product.getType()) && !findPrimary)
            throw new IllegalArgumentException("Main product cannot be ordered as addition: invalid extra: " + product.getName());
    }

    /**
     * Validates if product is eligible for bonus
     * @param product - product to validate
     * @throws IllegalArgumentException when product is not eligible for bonus
     */
    public static void validateIfProductIsEligibleForBonus(Product product) {
        if (!ProductType.DRINK.equals(product.getType()))
            throw new IllegalArgumentException("Only beverage can be collected for free: invalid product: " + product.getName());
    }
}
