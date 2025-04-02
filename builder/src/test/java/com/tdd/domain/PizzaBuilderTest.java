package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PizzaBuilderTest {
    PizzaBuilder builder = new PizzaBuilder();

    @Test
    void build_pizza_with_size() {
        Pizza pizza = builder.size(PizzaSize.SMALL).build();

        assertEquals(PizzaSize.SMALL, pizza.getSize());
        assertFalse(pizza.getCheese());
        assertFalse(pizza.getPeperoni());
    }

    @Test
    void build_pizza_with_cheese() {
        Pizza pizza = builder.cheese().build();

        assertTrue(pizza.getCheese());
        assertEquals(PizzaSize.SMALL, pizza.getSize());
        assertFalse(pizza.getPeperoni());
    }

    @Test
    void build_pizza_with_peperoni() {
        Pizza pizza = builder.peperoni().build();

        assertTrue(pizza.getPeperoni());
        assertEquals(PizzaSize.SMALL, pizza.getSize());
        assertFalse(pizza.getCheese());
    }

    @Test
    void build_pizza_with_size_and_cheese() {
        Pizza pizza = builder
                .size(PizzaSize.LARGE)
                .cheese()
                .build();

        assertEquals(PizzaSize.LARGE, pizza.getSize());
        assertTrue(pizza.getCheese());
        assertFalse(pizza.getPeperoni());
    }

    @Test
    void build_pizza_with_size_and_cheese_and_peperoni() {
        Pizza pizza = builder
                .size(PizzaSize.LARGE)
                .cheese()
                .peperoni()
                .build();

        assertEquals(PizzaSize.LARGE, pizza.getSize());
        assertTrue(pizza.getCheese());
        assertTrue(pizza.getPeperoni());
    }

    @Test
    void build_pizza_with_cheese_and_peperoni() {
        Pizza pizza = builder
                .cheese()
                .peperoni()
                .build();

        assertEquals(PizzaSize.SMALL, pizza.getSize());
        assertTrue(pizza.getCheese());
        assertTrue(pizza.getPeperoni());
    }

    @Test
    void build_pizza() {
        Pizza pizza = builder.build();

        assertEquals(PizzaSize.SMALL, pizza.getSize());
        assertFalse(pizza.getCheese());
        assertFalse(pizza.getPeperoni());
    }
}
