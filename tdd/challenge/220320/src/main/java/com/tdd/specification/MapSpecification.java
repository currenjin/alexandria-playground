package com.tdd.specification;

import java.util.Map;

public class MapSpecification implements Specification {
    private String expectedKey;
    private String expectedValue;

    public MapSpecification(String expectedKey, String expectedValue) {
        this.expectedKey = expectedKey;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return expectedValue.equals(((Map<String, Object>) object).get(expectedKey));
    }
}
