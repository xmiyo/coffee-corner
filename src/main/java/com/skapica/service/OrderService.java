package com.skapica.service;

import com.skapica.model.Order;
import com.skapica.model.Product;
import com.skapica.model.ProductType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Order order = new Order(products);
        applyBonuses(order);
        return order;
    }

    private static void applyBonuses(Order order) {
        applyFreeExtra(order);
        applyFreeDrinks(order);
    }

    private static void applyFreeDrinks(Order order) {
        List<Product> drinks = filterByType(order, ProductType.DRINK)
                .filter(product -> !product.isBonusProduct()).toList();
        int step = 5;
        if (drinks.size() >= step) {
            int currentIndex = step - 1;
            while (currentIndex <= drinks.size() - 1) {
                drinks.get(currentIndex).setBonusProduct(true);
                currentIndex += step;
            }
        }
        order.setBonusPoints(drinks.size() % step);
    }

    private static void applyFreeExtra(Order order) {
        List<Product> processedDrinks = new ArrayList<>();
        List<Product> processedSnacks = new ArrayList<>();

        Optional<Product> drink = getNextDrinkWithExtraIngredient(order, processedDrinks);
        Optional<Product> snack = getNextSnack(order, processedSnacks);

        while (drink.isPresent() && snack.isPresent()){
            drink.get().getExtras().get(0).setBonusProduct(true);

            processedDrinks.add(drink.get());
            processedSnacks.add(snack.get());

            drink = getNextDrinkWithExtraIngredient(order, processedDrinks);
            snack = getNextSnack(order, processedSnacks);

        }
    }

    private static Optional<Product> getNextSnack(Order order, List<Product> processedSnacks) {
        return filterByTypeIgnoringProducts(order, ProductType.SNACK, processedSnacks)
                .findFirst();
    }

    private static Optional<Product> getNextDrinkWithExtraIngredient(Order order, List<Product> processedDrinks) {
        return filterByTypeIgnoringProducts(order, ProductType.DRINK, processedDrinks)
                .filter(product -> product.getExtras() != null)
                .findFirst();
    }

    private static Stream<Product> filterByType(Order order, ProductType type) {
        return order.getProducts().stream().filter(product -> product.getType().equals(type));
    }

    private static Stream<Product> filterByTypeIgnoringProducts(Order order, ProductType type, List<Product> ignoredProducts) {
        return filterByType(order, type).filter(product -> !ignoredProducts.contains(product));
    }

}
