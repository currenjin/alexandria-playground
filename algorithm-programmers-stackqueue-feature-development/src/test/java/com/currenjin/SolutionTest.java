package com.currenjin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SolutionTest {
    @Test
    void sample1() {
        int[] actual = new Solution().solution(new int[]{93, 30, 55}, new int[]{1, 30, 5});
        assertArrayEquals(new int[]{2, 1}, actual);
    }

    @Test
    void sample2() {
        int[] actual = new Solution().solution(
                new int[]{95, 90, 99, 99, 80, 99},
                new int[]{1, 1, 1, 1, 1, 1}
        );
        assertArrayEquals(new int[]{1, 3, 2}, actual);
    }
}
