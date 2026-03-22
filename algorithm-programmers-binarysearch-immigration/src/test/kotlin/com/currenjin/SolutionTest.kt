package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SolutionTest {
    @Test
    fun sample1() {
        assertEquals(28L, Solution().solution(6, intArrayOf(7, 10)))
    }

    @Test
    fun oneInspector() {
        assertEquals(50L, Solution().solution(10, intArrayOf(5)))
    }
}
