package com.skapica.model;

import java.math.BigDecimal;

public class Product {
    private final String name;
    private final ProductType type;
    private final BigDecimal price;

    public Product(String name, ProductType type, BigDecimal price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public ProductType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }
}