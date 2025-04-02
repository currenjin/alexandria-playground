package com.tdd.domain.expression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {
    ExpressionVisitor visitor = new EvaluationVisitor();

    @Test
    void number_expression() {
        Expression number = new NumberExpression(5);

        int result = number.accept(visitor);

        assertEquals(5, result);
    }

    @Test
    void addition_expression() {
        Expression left = new NumberExpression(5);
        Expression right = new NumberExpression(5);
        Expression addition = new AdditionExpression(left, right);

        int result = addition.accept(visitor);

        assertEquals(10, result);
    }

    @Test
    void multiplication_expression() {
        Expression left = new NumberExpression(5);
        Expression right = new NumberExpression(5);
        Expression multiplication = new MultiplicationExpression(left, right);

        int result = multiplication.accept(visitor);

        assertEquals(25, result);
    }

    @Test
    void complex_expression() {
        Expression number1 = new NumberExpression(2);
        Expression number2 = new NumberExpression(3);
        Expression number3 = new NumberExpression(4);
        Expression addition = new AdditionExpression(number1, number2);
        Expression multiplication = new MultiplicationExpression(addition, number3);

        int result = multiplication.accept(visitor);

        assertEquals(20, result);
    }
}