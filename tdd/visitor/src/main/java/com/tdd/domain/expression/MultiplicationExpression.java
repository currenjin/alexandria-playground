package com.tdd.domain.expression;

public class MultiplicationExpression implements Expression {
    public MultiplicationExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    private final Expression left;
    private final Expression right;

    @Override
    public int accept(ExpressionVisitor visitor) {
        return visitor.visit(this);
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}
