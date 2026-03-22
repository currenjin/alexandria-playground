package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SolutionTest {
    @Test
    fun sample1() {
        assertEquals(3, Solution().solution(intArrayOf(3, 0, 6, 1, 5)))
    }

    @Test
    fun edgeCase() {
        assertEquals(4, Solution().solution(intArrayOf(10, 8, 5, 4, 3)))
    }
}
