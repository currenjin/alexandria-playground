package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PizzaTest {
    @Test
    void pizza() {
        Pizza smallPizza = new Pizza(PizzaSize.SMALL);
        Pizza largePizza = new Pizza(PizzaSize.LARGE);

        assertEquals(PizzaSize.SMALL, smallPizza.getSize());
        assertEquals(PizzaSize.LARGE, largePizza.getSize());
    }

    @Test
    void pizza_with_cheese() {
        Pizza pizza = new Pizza(PizzaSize.SMALL, true);
        assertTrue(pizza.getCheese());
    }

    @Test
    void pizza_without_cheese() {
        Pizza pizza = new Pizza(PizzaSize.SMALL, false);
        assertFalse(pizza.getCheese());
    }

    @Test
    void pizza_with_peperoni() {
        Pizza pizza = new Pizza(PizzaSize.SMALL, false, true);
        assertTrue(pizza.getPeperoni());
    }

    @Test
    void pizza_without_peperoni() {
        Pizza pizza = new Pizza(PizzaSize.SMALL, false, false);
        assertFalse(pizza.getPeperoni());
    }
}
