package com.tdd.domain.expression;

public interface ExpressionVisitor {
    int visit(NumberExpression expression);
    int visit(AdditionExpression expression);
    int visit(MultiplicationExpression expression);
}
