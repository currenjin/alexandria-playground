package com.tdd.domain.expression;

public class EvaluationVisitor implements ExpressionVisitor {
    @Override
    public int visit(NumberExpression expression) {
        return expression.getValue();
    }

    @Override
    public int visit(AdditionExpression expression) {
        return expression.getLeft().accept(this) + expression.getRight().accept(this);
    }

    @Override
    public int visit(MultiplicationExpression expression) {
        return expression.getLeft().accept(this) * expression.getRight().accept(this);
    }
}
