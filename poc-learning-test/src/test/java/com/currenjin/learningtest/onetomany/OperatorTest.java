package com.currenjin.learningtest.onetomany;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OperatorTest {

    @Test
    void sum_1() {
        assertThat(Operator.sum(5)).isEqualTo(5);
    }

    @Test
    void sum_2() {
        assertThat(Operator.sum(Lists.newArrayList(5, 7))).isEqualTo(12);
    }
}