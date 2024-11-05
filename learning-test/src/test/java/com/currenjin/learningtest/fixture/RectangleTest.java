package com.currenjin.learningtest.fixture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    Rectangle empty;

    @BeforeEach
    void setUp() {
        empty = new Rectangle(0, 0, 0, 0);
    }

    @Test
    void empty() {
        assertThat(empty.isEmpty()).isTrue();
    }

    @Test
    void width() {
        assertThat(empty.getWidth()).isEqualTo(0.0);
    }
}