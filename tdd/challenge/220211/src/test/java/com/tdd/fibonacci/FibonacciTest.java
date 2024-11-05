package com.tdd.fibonacci;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FibonacciTest {


    @Test
    void name() {
        /* 0, 1, 1, 2, 3, 5, 8, 13, 21 */

        int[][] cases = {{0, 0}, {1, 1}, {2, 1}, {3, 2}, {4, 3}, {5, 5}, {6, 8}, {7, 13}};

        for (int i = 0; cases.length > i; i++) {
            assertThat(Fibonacci.fib(cases[i][0])).isEqualTo(cases[i][1]);
        }
    }

    @ParameterizedTest
    @CsvSource(value = {"0, 0", "1, 1", "2, 1", "3, 2", "4, 3", "5, 5", "6, 8", "7, 13"})
    void name2(int input, int output) {
        assertThat(Fibonacci.fib(input)).isEqualTo(output);
    }
}
