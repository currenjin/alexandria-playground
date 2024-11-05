package com.tdd.util.spec;

import java.util.Map;

public class MapCondition implements Condition {

    public static Condition expected(String expectedKey, String expectedValue) {
        return new MapCondition(expectedKey, expectedValue);
    }

    private final String expectedKey;
    private final String expectedValue;

    public MapCondition(String expectedKey, String expectedValue) {
        this.expectedKey = expectedKey;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean isSatisfy(Object object) {
        if (object instanceof Map) {
            Map<String, String> map = (Map<String, String>) object;
            return expectedValue.equals(map.get(expectedKey));
        }

        return false;
    }
}
