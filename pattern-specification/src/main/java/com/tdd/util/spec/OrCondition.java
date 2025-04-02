package com.tdd.util.spec;

public class OrCondition implements Condition {
    private Condition left;
    private Condition right;

    public OrCondition(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return left.isSatisfy(object) || right.isSatisfy(object);
    }
}
