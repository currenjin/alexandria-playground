package com.tdd.util.spec;

import java.util.Map;

public class LessThanEqualCondition implements Condition {
    private String expectedKey;
    private int expectedValue;

    public LessThanEqualCondition(String expectedKey, int expectedValue) {
        this.expectedKey = expectedKey;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return ((Map<String, Integer>) object).get(expectedKey) <= expectedValue;
    }
}
