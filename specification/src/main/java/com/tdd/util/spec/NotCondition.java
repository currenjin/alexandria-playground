package com.tdd.util.spec;

import java.util.Map;

public class NotCondition implements Condition {
    private final Condition condition;

    public NotCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return !condition.isSatisfy(object);
    }
}
