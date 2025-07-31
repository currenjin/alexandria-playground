package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactorialTest {
    @Test
    void factorial_test() {
        assertEquals(0, fac(0));
        assertEquals(1, fac(1));
    }

    private int fac(int n) {
        return n;
    }
}
