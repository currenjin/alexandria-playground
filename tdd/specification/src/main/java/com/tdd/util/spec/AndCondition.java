package com.tdd.util.spec;

public class AndCondition implements Condition {

    private final Condition left;
    private final Condition right;

    public AndCondition(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return this.left.isSatisfy(object) && this.right.isSatisfy(object);
    }
}
