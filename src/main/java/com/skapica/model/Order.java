package com.skapica.model;

import java.util.List;

public class Order {
    private final List<Product> products;
    private Integer bonusPoints;

    public Order(List<Product> products){
        this.products = products;
    }
    public List<Product> getProducts() {
        return products;
    }

    public Integer getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(Integer bonusPoints) {
        this.bonusPoints = bonusPoints;
    }
}
