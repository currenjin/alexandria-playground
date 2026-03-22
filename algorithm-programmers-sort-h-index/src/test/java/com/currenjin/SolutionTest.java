package com.currenjin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
    @Test
    void sample1() {
        assertEquals(3, new Solution().solution(new int[]{3, 0, 6, 1, 5}));
    }

    @Test
    void edgeCase() {
        assertEquals(4, new Solution().solution(new int[]{10, 8, 5, 4, 3}));
    }
}
