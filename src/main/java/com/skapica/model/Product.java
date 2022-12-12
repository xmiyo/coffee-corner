package com.skapica.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.skapica.service.ProductService.*;

public class Product {
    private String name;
    private final ProductType type;
    private final BigDecimal price;
    private List<Product> extras;
    private boolean isBonusProduct;

    public Product(String name, ProductType type, BigDecimal price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public List<Product> getExtras() {
        return extras;
    }

    public String getName() {
        if (extras != null) {
            String extrasString = extras.stream().map(Product::getName).collect(Collectors.joining(" " + EXTRAS_JOIN_STRING + " "));
            name = String.format("%s %s %s", name, PRODUCTS_JOIN_STRING, extrasString);
        }
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
        else if (extras != null) {
            BigDecimal extrasPrice = BigDecimal.ZERO;
            for (Product product : extras) {
                extrasPrice = extrasPrice.add(product.getPrice());
            }
            return price.add(extrasPrice);
        } else
            return price;
    }

    public boolean isBonusProduct() {
        return isBonusProduct;
    }

    public void setBonusProduct(boolean bonusProduct) {
        isBonusProduct = bonusProduct;
    }

    public void addExtra(Product extra) {
        if (this.extras == null) {
            extras = new ArrayList<>();
        }
        extras.add(extra);
    }
}