package com.skapica.model;

import java.math.BigDecimal;

import static com.skapica.service.ProductService.BONUS_PREFIX;
import static com.skapica.service.ProductService.PRODUCTS_JOIN_STRING;

public class Product {
    private String name;
    private final ProductType type;
    private final BigDecimal price;
    private Product extra;
    private boolean isBonusProduct;

    public Product(String name, ProductType type, BigDecimal price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        if (extra != null)
            name = String.format("%s %s %s", name, PRODUCTS_JOIN_STRING, extra.getName());
        if (isBonusProduct)
            name = String.format("%s (%s)", name, BONUS_PREFIX);
        return name;
    }

    public ProductType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        if (isBonusProduct)
            return BigDecimal.ZERO;
        else if (extra != null)
            return price.add(extra.getPrice());
        else
            return price;
    }

    public void setBonusProduct(boolean bonusProduct) {
        isBonusProduct = bonusProduct;
    }

    public void setExtra(Product extra) {
        this.extra = extra;
    }
}