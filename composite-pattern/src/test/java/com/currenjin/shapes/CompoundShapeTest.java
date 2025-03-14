package com.currenjin.shapes;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompoundShapeTest {

    private static final Color COLOR = Color.BLACK;
    private static final int X = 0;
    private static final int Y = 0;
    private static final int WIDTH = 0;
    private static final int HEIGHT = 0;

    @Test
    void X_Y() {
        Shape dot = new Dot(X, Y, COLOR);
        Shape rectangle = new Rectangle(X, Y, WIDTH, HEIGHT, COLOR);

        Shape compound = new CompoundShape(dot, rectangle);

        assertEquals(X, compound.getX());
        assertEquals(Y, compound.getY());
    }
}
