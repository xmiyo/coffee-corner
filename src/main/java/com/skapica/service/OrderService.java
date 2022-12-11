package com.skapica.service;

import com.skapica.model.Order;
import com.skapica.model.Product;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {

    /**
     * Creates {@link Order} from command line arguments
     *
     * @param args products names passed to application
     * @return {@link Order} object containing list of {@link Product} items
     * @throws IllegalArgumentException when input array is null or empty or when any of items in collection is not found
     */
    public static Order createOrderFromCommandLineArguments(String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Cannot create order from empty product list");
        }
        String itemsAsString = String.join(" ", args);
        String[] inputItems = itemsAsString.split(",");
        List<Product> products = Arrays.stream(inputItems)
                .map(String::trim)
                .map(ProductService::findProduct)
                .collect(Collectors.toList());
        return new Order(products);
    }
}
