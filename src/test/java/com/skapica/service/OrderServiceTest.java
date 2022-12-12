package com.skapica.service;

import com.skapica.model.Order;
import com.skapica.model.Product;
import com.skapica.model.ProductType;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderServiceTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenCreatingOrderFromNullSource() {
        OrderService.createOrderFromCommandLineArguments(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenCreatingOrderFromEmptySource() {
        OrderService.createOrderFromCommandLineArguments(new String[]{});
    }

    @Test
    public void shouldCreateOrderFromOneItem() {
        Order order = OrderService.createOrderFromCommandLineArguments(new String[]{"Bacon Roll"});
        assertNotNull(order);
        assertEquals(1, order.getProducts().size());
    }

    @Test
    public void shouldCreateOrderFromMultipleItems() {
        String productList = "Bacon Roll, Small Coffee";
        Order order = OrderService.createOrderFromCommandLineArguments(productList.split(" "));
        assertNotNull(order);
        assertEquals(2, order.getProducts().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateOrderWhenAtLeastOneProductNotFound() {
        String productList = "Bacon Roll, Banana";
        OrderService.createOrderFromCommandLineArguments(productList.split(" "));
    }

    @Test
    public void shouldApplyFreeExtraForOneDrinkWithExtraAndOneSnack() {
        String productList = "small coffee with foamed milk, bacon roll";
        Order order = OrderService.createOrderFromCommandLineArguments(productList.split(" "));
        assertNotNull(order);
        assertEquals(2, order.getProducts().size());
        Product drink = order.getProducts().stream().filter(product -> product.getType().equals(ProductType.DRINK)).findFirst().orElse(null);
        assertNotNull(drink);
        assertEquals(BigDecimal.ZERO, drink.getExtra().getPrice());
    }

    @Test
    public void onlyPriceOfExtraShouldBeZeroWhenApplyingExtraBonus() {
        String productList = "small coffee with foamed milk, bacon roll";
        Order order = OrderService.createOrderFromCommandLineArguments(productList.split(" "));
        assertNotNull(order);
        assertEquals(2, order.getProducts().size());
        Product drink = order.getProducts().stream().filter(product -> product.getType().equals(ProductType.DRINK)).findFirst().orElse(null);
        assertNotNull(drink);
        assertEquals(BigDecimal.ZERO, drink.getExtra().getPrice());
        assertEquals(1, drink.getPrice().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldApplyFreeExtraForMoreThanOneDrinkWithExtraAndMoreThanOneSnack() {
        String productList = "small coffee with foamed milk, bacon roll, bacon roll, large coffee with foamed milk";
        Order order = OrderService.createOrderFromCommandLineArguments(productList.split(" "));
        assertNotNull(order);
        assertEquals(4, order.getProducts().size());
        List<Product> drinks = order.getProducts().stream()
                .filter(product -> product.getType().equals(ProductType.DRINK))
                .filter(product -> product.getExtra().getPrice().equals(BigDecimal.ZERO)).toList();
        assertEquals(2, drinks.size());
    }

    @Test
    public void shouldNotApplyFreeExtraForDrinkWithExtraWhenNoSnack() {
        String productList = "small coffee with foamed milk, orange juice, orange juice";
        Order order = OrderService.createOrderFromCommandLineArguments(productList.split(" "));
        assertNotNull(order);
        assertEquals(3, order.getProducts().size());
        List<Product> drinks = order.getProducts().stream()
                .filter(product -> product.getType().equals(ProductType.DRINK)).toList();
        assertEquals(3, drinks.size());
        List<Product> bonusDrinks = drinks.stream().filter(Product::isBonusProduct).toList();
        assertEquals(0, bonusDrinks.size());
    }

    @Test
    public void shouldApplyFreeDrinkBonusForFiveDrinksInOrder() {
        String productList = "small coffee, bacon roll, large coffee, medium coffee, small coffee, small coffee";
        Order order = OrderService.createOrderFromCommandLineArguments(productList.split(" "));
        assertNotNull(order);
        assertEquals(6, order.getProducts().size());
        List<Product> drinks = order.getProducts().stream()
                .filter(product -> product.getType().equals(ProductType.DRINK)).toList();
        assertEquals(5, drinks.size());
        List<Product> bonusDrinks = drinks.stream().filter(Product::isBonusProduct).toList();
        assertEquals(1, bonusDrinks.size());
    }

    @Test
    public void shouldApplyFreeDrinkBonusToTwoDrinksForElevenDrinksInOrder() {
        String productList = "small coffee, bacon roll, large coffee, medium coffee, small coffee, small coffee, small coffee, small coffee, small coffee, small coffee, small coffee, small coffee";
        Order order = OrderService.createOrderFromCommandLineArguments(productList.split(" "));
        assertNotNull(order);
        assertEquals(12, order.getProducts().size());
        List<Product> drinks = order.getProducts().stream()
                .filter(product -> product.getType().equals(ProductType.DRINK)).toList();
        assertEquals(11, drinks.size());
        List<Product> bonusDrinks = drinks.stream().filter(Product::isBonusProduct).toList();
        assertEquals(2, bonusDrinks.size());
    }

    @Test
    public void shouldNotGiveBonusPointsWhenApplyingFreeDrinkBonus() {
        String productList = "small coffee, small coffee, small coffee, small coffee, small coffee with foamed milk";
        Order order = OrderService.createOrderFromCommandLineArguments(productList.split(" "));
        assertNotNull(order);
        assertEquals(5, order.getProducts().size());
        List<Product> drinks = order.getProducts().stream()
                .filter(product -> product.getType().equals(ProductType.DRINK)).toList();
        assertEquals(5, drinks.size());
        List<Product> bonusDrinks = drinks.stream().filter(Product::isBonusProduct).toList();
        assertEquals(1, bonusDrinks.size());
        assertEquals(0, (int) order.getBonusPoints());
    }

    @Test
    public void shouldGiveBonusPointsWhenApplyingFreeDrinkBonusAndNotAllItemsUsedForBonus() {
        String productList = "small coffee, small coffee, small coffee, small coffee, small coffee with foamed milk, small coffee, small coffee";
        Order order = OrderService.createOrderFromCommandLineArguments(productList.split(" "));
        assertNotNull(order);
        assertEquals(7, order.getProducts().size());
        List<Product> drinks = order.getProducts().stream()
                .filter(product -> product.getType().equals(ProductType.DRINK)).toList();
        assertEquals(7, drinks.size());
        List<Product> bonusDrinks = drinks.stream().filter(Product::isBonusProduct).toList();
        assertEquals(1, bonusDrinks.size());
        assertEquals(2, (int) order.getBonusPoints());
    }

    @Test
    public void pirceOfBonusDringShouldBeZeroWhenApplyinFreeDrinkBonus() {
        String productList = "small coffee, small coffee, small coffee, small coffee, small coffee with foamed milk";
        Order order = OrderService.createOrderFromCommandLineArguments(productList.split(" "));
        assertNotNull(order);
        assertEquals(5, order.getProducts().size());
        List<Product> drinks = order.getProducts().stream()
                .filter(product -> product.getType().equals(ProductType.DRINK)).toList();
        assertEquals(5, drinks.size());
        List<Product> bonusDrinks = drinks.stream().filter(Product::isBonusProduct).toList();
        assertEquals(1, bonusDrinks.size());
        Product bonusDrink = bonusDrinks.get(0);
        assertEquals(BigDecimal.ZERO, bonusDrink.getPrice());
    }
}