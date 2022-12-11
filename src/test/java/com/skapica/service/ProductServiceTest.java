package com.skapica.service;

import com.skapica.model.Product;
import com.skapica.model.ProductType;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class ProductServiceTest {

    @Test
    public void loadAllProductsShouldLoadProductsFromFile() {
        List<Product> products = ProductService.loadAllProducts("products.csv");
        assertFalse(products.isEmpty());
        assertEquals(8, products.size());
    }

    @Test
    public void loadAllProductsShouldReturnEmptyProductListWhenFileIsNull() {
        List<Product> products = ProductService.loadAllProducts(null);
        assertTrue(products.isEmpty());
    }

    @Test
    public void loadAllProductsShouldReturnEmptyProductListWhenFileNotFound() {
        List<Product> products = ProductService.loadAllProducts("notExisting.csv");
        assertTrue(products.isEmpty());
    }

    @Test
    public void loadAllProductsShouldReturnEmptyProductListWhenItemDefinitionIsNotComplete() {
        List<Product> products = ProductService.loadAllProducts("itemIncomplete.csv");
        assertTrue(products.isEmpty());
    }

    @Test
    public void loadAllProductsShouldReturnEmptyProductListWhenItemDefinitionHasTooManyValues() {
        List<Product> products = ProductService.loadAllProducts("itemWithTooManyProperties.csv");
        assertTrue(products.isEmpty());
    }

    @Test
    public void loadAllProductsShouldReturnEmptyProductListWhenItemHasWrongPriceFormat() {
        List<Product> products = ProductService.loadAllProducts("itemWithWrongPriceFormat.csv");
        assertTrue(products.isEmpty());
    }

    @Test
    public void loadAllProductsShouldReturnEmptyProductListWhenItemHasUnknownCategory() {
        List<Product> products = ProductService.loadAllProducts("itemWithUnknownCategory.csv");
        assertTrue(products.isEmpty());
    }

    @Test
    public void findProductShouldReturnProduct() {
        Product product = ProductService.findProduct("Small Coffee");
        assertNotNull(product);
        assertEquals(ProductType.DRINK, product.getType());
        assertEquals(new BigDecimal("2.5"), product.getPrice());
        assertEquals("Small Coffee", product.getName());
    }

    @Test
    public void findProductShouldIgnoreCaseAndReturnProduct() {
        Product product = ProductService.findProduct("small coffee");
        assertNotNull(product);
        assertEquals("Small Coffee", product.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findProductShouldThrowIllegalArgumentExceptionWhenProductNotFound() {
        ProductService.findProduct("unknown product");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findProductShouldThrowIllegalArgumentExceptionWhenOrderingExtraWithoutMainProduct() {
        ProductService.findProduct("Extra milk");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findProductShouldThrowIllegalArgumentExceptionWhenOrderingMainProductAsExtra() {
        ProductService.findProduct("Small coffee with large coffee");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findProductShouldThrowIllegalArgumentExceptionWhenOrderingAmbiguousMainProduct() {
        ProductService.findProduct("coffee");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findProductShouldThrowIllegalArgumentExceptionWhenOrderingAmbiguousExtraProduct() {
        ProductService.findProduct("small coffee with milk");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findProductShouldThrowIllegalArgumentExceptionWhenOrderingExtraToProductDifferentThanCoffee() {
        ProductService.findProduct("orange juice with extra milk");
    }

    @Test
    public void shouldFindCoffeeProductWithExtra() {
        ProductService.findProduct("medium coffee with extra milk");
    }

    @Test
    public void priceOfProductWithExtraShouldBeHigherThanBaseProductPrice() {
        Product base = ProductService.findProduct("medium coffee");
        Product withExtra = ProductService.findProduct("medium coffee with extra milk");
        assertEquals(-1, base.getPrice().compareTo(withExtra.getPrice()));
    }
}