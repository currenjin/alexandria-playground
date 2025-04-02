package com.tdd.util.spec;

import java.util.Map;

public class GreaterThanEqual implements Condition {
    private final String expectedKey;
    private final int expectedValue;

    public GreaterThanEqual(String expectedKey, int expectedValue) {
        this.expectedKey = expectedKey;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return ((Map<String, Integer>) object).get(expectedKey) >= expectedValue;
    }
}
