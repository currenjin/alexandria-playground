package com.tdd.specification;

import java.util.Map;

public class LessThanSpecification implements Specification {
    private String expectedKey;
    private int expectedValue;

    public LessThanSpecification(String expectedKey, int expectedValue) {
        this.expectedKey = expectedKey;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return expectedValue > ((Map<String, Integer>) object).get(expectedKey);
    }
}
