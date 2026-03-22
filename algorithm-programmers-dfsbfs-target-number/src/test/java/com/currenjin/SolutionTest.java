package com.currenjin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
    @Test
    void sample1() {
        assertEquals(5, new Solution().solution(new int[]{1, 1, 1, 1, 1}, 3));
    }

    @Test
    void sample2() {
        assertEquals(2, new Solution().solution(new int[]{4, 1, 2, 1}, 4));
    }
}
