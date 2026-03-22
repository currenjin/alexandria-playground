package com.currenjin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolutionTest {
    @Test
    void trueCase() {
        assertTrue(new Solution().solution("()()"));
    }

    @Test
    void falseCase() {
        assertFalse(new Solution().solution("(())("));
    }
}
