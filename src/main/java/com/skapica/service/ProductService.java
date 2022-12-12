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

public class ProductService {
    public static final String PRODUCTS_SOURCE = "products.csv";
    public static final String PRODUCTS_JOIN_STRING = "with";
    public static final String EXTRAS_JOIN_STRING = "and";
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
        if (productName.trim().startsWith(BONUS_PREFIX)) {
            productName = productName.replace(BONUS_PREFIX, "");
            isBonusProduct = true;
        }
        Product product;
        if (productName.contains(PRODUCTS_JOIN_STRING)) {
            // handle product with extra
            String[] items = productName.split(PRODUCTS_JOIN_STRING);
            product = findProduct(items[0], true);
            ProductValidator.validateIfExtrasCanBeAdded(product);
            if (items[1].contains(EXTRAS_JOIN_STRING)) {
                //handle multiple extras
                String[] extras = items[1].split(EXTRAS_JOIN_STRING);
                ProductValidator.validateExtrasSize(extras);
                Arrays.stream(extras).forEach(extraItem -> {
                    Product extra = findProduct(extraItem, false);
                    product.addExtra(extra);
                });
            } else {
                //handle single extra
                Product extra = findProduct(items[1], false);
                product.addExtra(extra);
            }
        } else {
            // handle simple product
            String finalProductName = productName;
            List<Product> matchingProducts = loadAllProducts(PRODUCTS_SOURCE).stream()
                    .filter(p -> p.getName().toLowerCase().contains(finalProductName.trim().toLowerCase())).toList();

            ProductValidator.validateIfProductExistsAndIsUnique(productName, matchingProducts);

            product = matchingProducts.get(0);

            ProductValidator.validateIfProductCanBeSold(findPrimary, product);
        }
        if (isBonusProduct) {
            ProductValidator.validateIfProductIsEligibleForBonus(product);
            product.setBonusProduct(true);
        }
        return product;
    }

    private static List<Product> loadAllProductsFromFile(String fileName) {
        List<Product> products = new ArrayList<>();
        try (InputStream in = App.class.getResourceAsStream("/" + fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))) {
            for (String line; (line = reader.readLine()) != null; ) {
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
}
