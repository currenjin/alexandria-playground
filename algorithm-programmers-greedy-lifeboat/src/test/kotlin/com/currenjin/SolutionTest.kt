package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SolutionTest {
    @Test
    fun sample1() {
        assertEquals(3, Solution().solution(intArrayOf(70, 50, 80, 50), 100))
    }

    @Test
    fun sample2() {
        assertEquals(3, Solution().solution(intArrayOf(70, 80, 50), 100))
    }
}
