package com.skapica.service;

import com.skapica.model.Order;
import org.junit.Test;

import static org.junit.Assert.*;

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
}