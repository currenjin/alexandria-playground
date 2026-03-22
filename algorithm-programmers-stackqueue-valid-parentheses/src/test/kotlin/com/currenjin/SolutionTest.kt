package com.currenjin

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SolutionTest {
    @Test
    fun trueCase() {
        assertTrue(Solution().solution("()()"))
    }

    @Test
    fun falseCase() {
        assertFalse(Solution().solution("(())("))
    }
}
