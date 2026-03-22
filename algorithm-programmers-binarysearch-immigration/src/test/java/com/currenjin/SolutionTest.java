package com.currenjin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
    @Test
    void sample1() {
        assertEquals(28L, new Solution().solution(6, new int[]{7, 10}));
    }

    @Test
    void oneInspector() {
        assertEquals(50L, new Solution().solution(10, new int[]{5}));
    }
}
