package com.currenjin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
    @Test
    void sample1() {
        String[][] clothes = {
                {"yellow_hat", "headgear"},
                {"blue_sunglasses", "eyewear"},
                {"green_turban", "headgear"}
        };
        assertEquals(5, new Solution().solution(clothes));
    }

    @Test
    void sample2() {
        String[][] clothes = {
                {"crow_mask", "face"},
                {"blue_sunglasses", "face"},
                {"smoky_makeup", "face"}
        };
        assertEquals(3, new Solution().solution(clothes));
    }
}
