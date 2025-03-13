package com.currenjin.shapes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

class ShapeTest {

    private static final int ZERO = 0;
    public static final int X = 1;
    public static final int Y = 1;
    public static final Color BLACK = Color.BLACK;

    @Nested
    class BaseShapeTest {
        private Shape baseShape;

        @BeforeEach
        void setUp() {
            baseShape = new BaseShape(X, Y, BLACK);
        }

        @Test
        void constructor_initial_values() {
            assertThat(baseShape.getX()).isEqualTo(X);
            assertThat(baseShape.getY()).isEqualTo(Y);
            assertThat(baseShape.getWidth()).isEqualTo(ZERO);
            assertThat(baseShape.getHeight()).isEqualTo(ZERO);
        }

        @Test
        void move() {
            baseShape.move(X, Y);

            assertThat(baseShape.getX()).isEqualTo(X + X);
            assertThat(baseShape.getY()).isEqualTo(Y + Y);
        }

        @Test
        void select() {
            assertThat(baseShape.isSelected()).isFalse();

            baseShape.select();
            assertThat(baseShape.isSelected()).isTrue();

            baseShape.unSelect();
            assertThat(baseShape.isSelected()).isFalse();

            baseShape.select();
            baseShape.select();
            assertThat(baseShape.isSelected()).isTrue();
        }

        @Test
        void is_inside_bound() {
            assertThat(baseShape.isInsideBounds(X, Y)).isFalse();
            assertThat(baseShape.isInsideBounds(X - 1, Y - 1)).isFalse();
            assertThat(baseShape.isInsideBounds(X + 1, Y + 1)).isFalse();
        }
    }

    @Nested
    class DotTest {
        public static final int DOT_SIZE = 3;

        Shape dot;

        @BeforeEach
        void setUp() {
            dot = new Dot(X, Y, BLACK);
        }

        @Test
        void constructor() {
            assertThat(dot.getX()).isEqualTo(X);
            assertThat(dot.getY()).isEqualTo(Y);
            assertThat(dot.getWidth()).isEqualTo(DOT_SIZE);
            assertThat(dot.getHeight()).isEqualTo(DOT_SIZE);
        }
    }
}