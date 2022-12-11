package com.skapica.model;

import java.math.BigDecimal;

import static com.skapica.service.ProductService.PRODUCTS_JOIN_STRING;

public class Product {
    private final String name;
    private final ProductType type;
    private final BigDecimal price;

    private Product extra;

    public Product(String name, ProductType type, BigDecimal price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        if (extra != null)
            return String.format("%s %s %s", name, PRODUCTS_JOIN_STRING, extra.getName());
        else
            return name;
    }

    public ProductType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        if (extra != null)
            return price.add(extra.getPrice());
        else
            return price;
    }

    public void setExtra(Product extra) {
        this.extra = extra;
    }
}