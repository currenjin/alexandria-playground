package com.currenjin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
    @Test
    void sample1() {
        int[][] maps = {
                {1, 0, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1},
                {0, 0, 0, 0, 1}
        };
        assertEquals(11, new Solution().solution(maps));
    }

    @Test
    void sample2() {
        int[][] maps = {
                {1, 0, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1},
                {1, 1, 1, 0, 0},
                {0, 0, 0, 0, 1}
        };
        assertEquals(-1, new Solution().solution(maps));
    }
}
