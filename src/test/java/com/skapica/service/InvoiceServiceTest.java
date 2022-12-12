package com.skapica.service;

import com.skapica.model.Order;
import com.skapica.model.Product;
import com.skapica.model.ProductType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvoiceServiceTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldDoNothingWhenForNullInvoice() {
        InvoiceService.printInvoice(null);
        Assert.assertEquals("", outContent.toString());
    }

    @Test
    public void shouldPrintInvoiceHeader() {
        InvoiceService.printInvoice(createOrder());
        String consoleOutput = outContent.toString();
        String[] lines = consoleOutput.split("\n");
        Assert.assertEquals("", lines[0]);
        assertTrue(lines[1].matches(getSeparatorPattern()));
        assertTrue(lines[2].contains("Charlene's Coffee Corner"));
        assertTrue(lines[3].matches(getSeparatorPattern()));
    }

    @Test
    public void shouldPrintPurchaseDateAndInvoiceNumber() {
        InvoiceService.printInvoice(createOrder());
        String consoleOutput = outContent.toString();
        String[] lines = consoleOutput.split("\n");
        assertTrue(lines[4].matches("^(Purchase date: )(\\d{2}/\\d{2}/\\d{4}) (\\d{2}:\\d{2})\r?$"));
        assertTrue(lines[5].matches("^(Invoice number: )(\\d{1,10})/\\d{4}\r?$"));
        assertTrue(lines[6].matches(getSeparatorPattern()));
    }

    @Test
    public void shouldPrintProductNameAndPriceForEachProduct() {
        Order order = createOrder();
        InvoiceService.printInvoice(order);
        String consoleOutput = outContent.toString();
        String[] lines = consoleOutput.split("\n");
        assertTrue(lines[7].startsWith(order.getProducts().get(0).getName()));
        assertTrue(lines[7].contains(order.getProducts().get(0).getPrice().toString()));
        assertTrue(lines[8].startsWith(order.getProducts().get(1).getName()));
        assertTrue(lines[8].contains(order.getProducts().get(1).getPrice().toString()));
        BigDecimal total = order.getProducts().stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        assertTrue(lines[10].startsWith("Total: "));
        assertTrue(lines[10].contains(total.toString()));
        assertTrue(lines[11].matches(getSeparatorPattern()));
    }

    @Test
    public void shouldPrintBonusPointsWhenBonusPointsGreaterThanZero() {
        Order order = createOrder();
        order.setBonusPoints(2);
        InvoiceService.printInvoice(order);
        String consoleOutput = outContent.toString();
        String[] lines = consoleOutput.split("\n");
        assertTrue(lines[12].startsWith("Bonus points: 2"));
    }

    @Test
    public void shouldNotPrintBonusPointsWhenBonusPointsEqualZero() {
        Order order = createOrder();
        order.setBonusPoints(0);
        InvoiceService.printInvoice(order);
        String consoleOutput = outContent.toString();
        String[] lines = consoleOutput.split("\n");
        Arrays.stream(lines).forEach(line -> assertFalse(line.contains("Bonus points:")));
    }

    @Test
    public void shouldNotPrintBonusPointsWhenBonusPointsEqualNull() {
        Order order = createOrder();
        order.setBonusPoints(null);
        InvoiceService.printInvoice(order);
        String consoleOutput = outContent.toString();
        String[] lines = consoleOutput.split("\n");
        Arrays.stream(lines).forEach(line -> assertFalse(line.contains("Bonus points:")));
    }

    private static String getSeparatorPattern() {
        return "^-{65}\r?$";
    }

    private Order createOrder(){
        List<Product> products = new ArrayList<>();
        Product p1 = new Product("coffee", ProductType.DRINK, new BigDecimal(1));
        Product p2 = new Product("snack", ProductType.SNACK, new BigDecimal(2));
        products.add(p1);
        products.add(p2);
        return new Order(products);
    }
}