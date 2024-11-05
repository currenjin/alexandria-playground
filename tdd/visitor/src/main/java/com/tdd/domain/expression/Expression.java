package com.tdd.domain.expression;

public interface Expression {
    int accept(ExpressionVisitor visitor);
}
