package com.currenjin.learningtest.triangulate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OperationTest {

    @Test
    void sum() {
        assertThat(Operator.plus(3, 1)).isEqualTo(4);
        assertThat(Operator.plus(3, 4)).isEqualTo(7);
    }
}
