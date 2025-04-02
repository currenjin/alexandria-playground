package com.challenge.tdd;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PairTest {
    @Test
    void testArrayEquals() {
        assertThat(new Object[] {"abc"}).isEqualTo(new Object[] {"abc"});
    }
}
