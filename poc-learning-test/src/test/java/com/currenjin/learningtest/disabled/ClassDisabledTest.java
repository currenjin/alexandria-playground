package com.currenjin.learningtest.disabled;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Disabled until bug has been fixed")
class ClassDisabledTest {

    @Test
    void add() {
        int actual = Operator.add(1, 3);

        assertThat(actual).isEqualTo(4);
    }
}