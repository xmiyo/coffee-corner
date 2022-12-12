package com.skapica.service;

import com.skapica.model.Order;
import com.skapica.model.Product;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvoiceService {

    /**
     * Prints invoice for given order.
     * When there is no products on the list, nothing is printed to console
     *
     * @param order order containing list of {@link Product} items
     */
    public static void printInvoice(Order order) {
        if (order != null) {
            DecimalFormat priceFormat = new DecimalFormat("0.00");
            String dateFormat = "dd/MM/yyyy HH:mm";

            //print header
            System.out.println("\n-----------------------------------------------------------------");
            System.out.println(addPadding("Charlene's Coffee Corner", 45));
            System.out.println("-----------------------------------------------------------------");
            System.out.println("Purchase date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat)));
            System.out.println("Invoice number: " + getInvoiceNumber());
            System.out.println("-----------------------------------------------------------------");

            //print purchase items
            String invoiceEntryFormat = "%s %s CHF";
            int itemNamePadding = -50;
            int itemPricePadding = 10;
            order.getProducts().forEach(product -> System.out.printf((invoiceEntryFormat) + "%n",
                    addPadding(product.getName(), itemNamePadding),
                    addPadding(priceFormat.format(product.getPrice()), itemPricePadding)));
            BigDecimal total = order.getProducts().stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println();
            System.out.printf((invoiceEntryFormat) + "%n",
                    addPadding("Total:", itemNamePadding),
                    addPadding(priceFormat.format(total), itemPricePadding));
            System.out.println("-----------------------------------------------------------------");

            //print bonus points
            Integer bonusPoints = order.getBonusPoints();
            if (bonusPoints != null && bonusPoints > 0)
                System.out.println("Bonus points: " + order.getBonusPoints());
        }
    }

    /**
     * Returns next invoice number for given calendar year
     * Currently number is fixed
     *
     * @return formatted invoice number
     */
    private static String getInvoiceNumber() {
        return String.format("%s/%s", 1, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));
    }

    /**
     * Adds padding of provided length to String value.
     * For negative padding, value is padded right.
     * For positive padding, value is padded left.
     * When value is longer than padding parameter, than no padding will be added.
     *
     * @param value   string value to apply padding
     * @param padding length of padding to add
     * @return Value with added padding.
     */
    private static String addPadding(String value, int padding) {
        return String.format("%1$" + padding + "s", value);
    }
}
