package com.tdd.domain;

public class PizzaBuilder {
    private PizzaSize size = PizzaSize.SMALL;
    private boolean cheese = false;
    private boolean peperoni = false;

    public PizzaBuilder size(PizzaSize size) {
        this.size = size;
        return this;
    }

    public PizzaBuilder cheese() {
        this.cheese = true;
        return this;
    }

    public PizzaBuilder peperoni() {
        this.peperoni = true;
        return this;
    }

    public Pizza build() {
        return new Pizza(size, cheese, peperoni);
    }
}
