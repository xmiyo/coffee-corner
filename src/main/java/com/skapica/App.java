package com.skapica;

import com.skapica.model.Order;
import com.skapica.service.InvoiceService;
import com.skapica.service.OrderService;

import java.util.Objects;
import java.util.logging.Logger;

public class App {
    private static Logger log = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        if (Objects.isNull(args) || args.length == 0) {
            log.warning("Order cannot be processed. Provide at least one product name to make an order.");
        } else {
            try {
                Order order = OrderService.createOrderFromCommandLineArguments(args);
                InvoiceService.printInvoice(order);
                log.fine("Successfully processed order");
            } catch (IllegalArgumentException e) {
                log.severe("Order cannot be processed. Reason: " + e.getMessage());
            }
        }
    }

    public static void setLog(Logger log) {
        App.log = log;
    }
}
