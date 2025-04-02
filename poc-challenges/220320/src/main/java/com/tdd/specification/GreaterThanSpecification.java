package com.tdd.specification;

import java.util.Map;

public class GreaterThanSpecification implements Specification {
    private final String expectedKey;
    private final Integer expectedValue;

    public GreaterThanSpecification(String expectedKey, Integer expectedValue) {
        this.expectedKey = expectedKey;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return expectedValue < ((Map<String, Integer>) object).get(expectedKey);
    }
}
