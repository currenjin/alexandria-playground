package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SolutionTest {
    @Test
    fun sample1() {
        assertEquals(5, Solution().solution(intArrayOf(1, 1, 1, 1, 1), 3))
    }

    @Test
    fun sample2() {
        assertEquals(2, Solution().solution(intArrayOf(4, 1, 2, 1), 4))
    }
}
