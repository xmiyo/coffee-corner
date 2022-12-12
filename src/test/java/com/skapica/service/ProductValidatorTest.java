package com.skapica.service;

import com.skapica.model.Product;
import com.skapica.model.ProductType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductValidatorTest {

    @Test (expected = IllegalArgumentException.class)
    public void validateIfExtrasCanBeAddedShouldThrowIllegalArgumentExceptionWhenProductIsNotCoffee() {
        Product product = new Product("test", null, null);
        ProductValidator.validateIfExtrasCanBeAdded(product);
    }

    @Test
    public void validateIfExtrasCanBeAddedShouldDoNothingWhenProductIsCoffee() {
        Product product = new Product("coffee", null, null);
        ProductValidator.validateIfExtrasCanBeAdded(product);
    }

    @Test
    public void validateIfProductExistsAndIsUniqueShouldDoNothingWhenListHasOneItem() {
        Product product = new Product("coffee", null, null);
        List<Product> products = List.of(product);
        ProductValidator.validateIfProductExistsAndIsUnique("coffee", products);
    }

    @Test (expected = IllegalArgumentException.class)
    public void validateIfProductExistsAndIsUniqueShouldThrowIllegalArgumentExceptionWhenListHasMoreThanOneItem() {
        Product product1 = new Product("coffee", null, null);
        Product product2 = new Product("another coffee", null, null);
        List<Product> products = Arrays.asList(product1, product2);
        ProductValidator.validateIfProductExistsAndIsUnique("coffee", products);
    }

    @Test (expected = IllegalArgumentException.class)
    public void validateIfProductExistsAndIsUniqueShouldThrowIllegalArgumentExceptionWhenListIsEmpty() {
        ProductValidator.validateIfProductExistsAndIsUnique("coffee", Collections.emptyList());
    }

    @Test
    public void validateIfProductCanBeSoldShouldDoNothingWhenSellingDrinkAsMainProduct() {
        Product product = new Product(null, ProductType.DRINK, null);
        ProductValidator.validateIfProductCanBeSold(true, product);
    }

    @Test
    public void validateIfProductCanBeSoldShouldDoNothingWhenSellingSnackAsMainProduct() {
        Product product = new Product(null, ProductType.SNACK, null);
        ProductValidator.validateIfProductCanBeSold(true, product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateIfProductCanBeSoldShouldThrowIllegalArgumentExceptionWhenSellingExtraAsMainProduct() {
        Product product = new Product(null, ProductType.EXTRA, null);
        ProductValidator.validateIfProductCanBeSold(true, product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateIfProductCanBeSoldShouldThrowIllegalArgumentExceptionWhenSellingDrinkAsExtraProduct() {
        Product product = new Product(null, ProductType.DRINK, null);
        ProductValidator.validateIfProductCanBeSold(false, product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateIfProductCanBeSoldShouldThrowIllegalArgumentExceptionWhenSellingSnackAsExtraProduct() {
        Product product = new Product(null, ProductType.SNACK, null);
        ProductValidator.validateIfProductCanBeSold(false, product);
    }

    @Test
    public void validateIfProductCanBeSoldShouldDoNothingWhenSellingExtraAsExtraProduct() {
        Product product = new Product(null, ProductType.EXTRA, null);
        ProductValidator.validateIfProductCanBeSold(false, product);
    }

    @Test
    public void validateIfProductIsEligibleForBonusShouldDoNothingWhenApplyingBonusToDrink() {
        Product product = new Product(null, ProductType.DRINK, null);
        ProductValidator.validateIfProductIsEligibleForBonus(product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateIfProductIsEligibleForBonusShouldThrowIllegalArgumentExceptionWhenApplyingBonusToSnack() {
        Product product = new Product(null, ProductType.SNACK, null);
        ProductValidator.validateIfProductIsEligibleForBonus(product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateIfProductIsEligibleForBonusShouldThrowIllegalArgumentExceptionWhenApplyingBonusToExtra() {
        Product product = new Product(null, ProductType.EXTRA, null);
        ProductValidator.validateIfProductIsEligibleForBonus(product);
    }
}