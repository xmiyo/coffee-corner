package com.skapica.service;

import com.skapica.App;
import com.skapica.model.Product;
import com.skapica.model.ProductType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductService {
    public static final String PRODUCTS_SOURCE = "products.csv";
    public static final String PRODUCTS_JOIN_STRING = "with";
    public static final String BONUS_PREFIX = "bonus";
    private static final Logger log = Logger.getLogger(ProductService.class.getName());

    public static List<Product> loadAllProducts(String fileName) {
        return loadAllProductsFromFile(fileName);
    }

    public static Product findProduct(String productName) {
        return findProduct(productName, true);
    }

    private static Product findProduct(String productName, boolean findPrimary) {
        boolean isBonusProduct = false;
        if (productName.trim().startsWith(BONUS_PREFIX)){
            productName = productName.replace(BONUS_PREFIX,"");
            isBonusProduct = true;
        }
        Product product;
        if (productName.contains(PRODUCTS_JOIN_STRING)){
            // handle product with extra
            String[] items = productName.split(PRODUCTS_JOIN_STRING);
            product = findProduct(items[0], true);

            validateIfExtrasCanBeAdded(product);

            Product extra = findProduct(items[1], false);
            product.setExtra(extra);

        } else {
            // handle simple product
            String finalProductName = productName;
            List<Product> matchingProducts = loadAllProducts(PRODUCTS_SOURCE).stream()
                    .filter(p -> p.getName().toLowerCase().contains(finalProductName.trim().toLowerCase())).toList();

            validateIfProductExistsAndIsUnique(productName, matchingProducts);

            product = matchingProducts.get(0);

            validateIfProductCanBeSold(findPrimary, product);
        }
        if (isBonusProduct){
            validateIfProductIsEligibleForBonus(product);
            product.setBonusProduct(true);
        }
        return product;
    }

    private static List<Product> loadAllProductsFromFile(String fileName) {
        List<Product> products = new ArrayList<>();
        try (InputStream in = App.class.getResourceAsStream("/" + fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))) {
            for(String line; (line = reader.readLine()) != null; ) {
                String[] properties = line.split(",");
                if (properties.length != 3) {
                    String message = "Incorrect product definition: " + line;
                    log.severe(message);
                    throw new IllegalArgumentException(message);
                } else {
                    try {
                        String productName = properties[0];
                        ProductType productType = ProductType.valueOf(properties[1]);
                        BigDecimal productPrice = new BigDecimal(properties[2]);
                        Product product = new Product(productName, productType, productPrice);
                        products.add(product);
                    } catch (IllegalArgumentException e) {
                        log.severe("Incorrect product definition: " + line);
                        throw e;
                    }
                }
            }
        } catch (NullPointerException | IOException | IllegalArgumentException e) {
            log.severe(String.format("Could not read product list. Error reading file: %s. reason: %s", fileName, e.getMessage()));
            return Collections.emptyList();
        }
        return products;
    }

    /**
     * Validates if extra ingredient can be added to product
     * @param product product to validate
     * @throws IllegalArgumentException when adding extras to product is not allowed
     */
    private static void validateIfExtrasCanBeAdded(Product product) {
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
    private static void validateIfProductExistsAndIsUnique(String productName, List<Product> matchingProducts) {
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
    private static void validateIfProductCanBeSold(boolean findPrimary, Product product) {
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
    private static void validateIfProductIsEligibleForBonus(Product product) {
        if (!ProductType.DRINK.equals(product.getType()))
            throw new IllegalArgumentException("Only beverage can be collected for free: invalid product: " + product.getName());
    }
}
