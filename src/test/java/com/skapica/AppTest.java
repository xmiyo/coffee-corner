package com.skapica;

import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppTest {

    private TestLogHandler handler;

    /**
     * overrides default application logger to capture and test output
     */
    @Before
    public void initLogger() {
        handler = new TestLogHandler();
        handler.setLevel(Level.ALL);
        Logger logger = Logger.getLogger("testLogger");
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        App.setLog(logger);
    }

    @Test
    public void shouldPrintNoItemsWarningMessageWhenCommandLineArgumentListIsNull() {
        runAppWithEmptyArgs(null);
    }

    @Test
    public void shouldPrintNoItemsWarningMessageWhenCommandLineArgumentListIsEmpty() {
        runAppWithEmptyArgs(new String[]{});
    }

    @Test
    public void shouldPrintErrorMessageForInvalidProduct() {
        String productsAsString = "Small Coffee, Invalid Product";
        App.main(productsAsString.split(" "));
        assertEquals(Level.SEVERE, handler.getLevel());
        assertTrue(handler.getLastMessage().startsWith("Order cannot be processed. Reason: "));
    }

    @Test
    public void shouldPrintSuccessMessageForValidProducts() {
        String productsAsString = "Small Coffee, Bacon Roll";
        App.main(productsAsString.split(" "));
        assertEquals(Level.FINE, handler.getLevel());
        assertEquals("Successfully processed order", handler.getLastMessage());
    }

    private void runAppWithEmptyArgs(String[] args) {
        App.main(args);
        assertEquals(Level.WARNING, handler.getLevel());
        assertEquals("Order cannot be processed. Provide at least one product name to make an order.", handler.getLastMessage());
    }
}
