package com.currenjin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
    @Test
    void sample1() {
        assertEquals(3, new Solution().solution(new int[]{70, 50, 80, 50}, 100));
    }

    @Test
    void sample2() {
        assertEquals(3, new Solution().solution(new int[]{70, 80, 50}, 100));
    }
}
