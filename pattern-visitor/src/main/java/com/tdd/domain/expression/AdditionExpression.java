package com.tdd.domain.expression;

public class AdditionExpression implements Expression {
    public AdditionExpression(Expression left, Expression right) {
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
