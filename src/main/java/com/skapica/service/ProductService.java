package com.skapica.service;

import com.skapica.App;
import com.skapica.model.Product;
import com.skapica.model.ProductType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ProductService {
    public static final String PRODUCTS_SOURCE = "products.csv";
    private static final Logger log = Logger.getLogger(ProductService.class.getName());

    public static List<Product> loadAllProducts(String fileName) {
        return loadAllProductsFromFile(fileName);
    }

    public static Product findProduct(String productName) {
        return loadAllProducts(PRODUCTS_SOURCE).stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No product found: " + productName));
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
}
