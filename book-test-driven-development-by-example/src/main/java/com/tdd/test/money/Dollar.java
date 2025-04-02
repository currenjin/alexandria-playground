package com.tdd.test.money;

public class Dollar extends Money {
    Dollar(int amount, String currency) {
        super(amount, currency);
    }

    public Money times(int multipler) {
        return new Money(amount * multipler, currency);
    }
}
