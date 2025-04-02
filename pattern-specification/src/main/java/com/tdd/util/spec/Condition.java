package com.tdd.util.spec;

public interface Condition {
    boolean isSatisfy(Object object);

    static Condition and(Condition left, Condition right) {
        return left.and(right);
    }

    static Condition or(Condition left, Condition right) {
        return left.or(right);
    }

    static Condition not(Condition condition) {
        return condition.not();
    }

    default Condition and(Condition right) {
        return new AndCondition(this, right);
    }

    default Condition or(Condition right) {
        return new OrCondition(this, right);
    }

    default Condition not() {
        return new NotCondition(this);
    }
}
