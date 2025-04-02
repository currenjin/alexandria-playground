package com.tdd.domain;

public class Pizza {
    private final PizzaSize size;
    private final boolean cheese;
    private final boolean peperoni;

    public Pizza(PizzaSize size) {
        this.size = size;
        this.cheese = false;
        this.peperoni = false;
    }

    public Pizza(PizzaSize size, boolean cheese) {
        this.size = size;
        this.cheese = cheese;
        this.peperoni = false;
    }

    public Pizza(PizzaSize size, boolean cheese, boolean peperoni) {
        this.size = size;
        this.cheese = cheese;
        this.peperoni = peperoni;
    }

    public PizzaSize getSize() {
        return this.size;
    }

    public boolean getCheese() {
        return this.cheese;
    }

    public boolean getPeperoni() {
        return this.peperoni;
    }
}
