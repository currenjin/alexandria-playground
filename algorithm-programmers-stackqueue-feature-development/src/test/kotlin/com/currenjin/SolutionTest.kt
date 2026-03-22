package com.currenjin

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class SolutionTest {
    @Test
    fun sample1() {
        val actual = Solution().solution(
            intArrayOf(93, 30, 55),
            intArrayOf(1, 30, 5)
        )
        assertArrayEquals(intArrayOf(2, 1), actual)
    }

    @Test
    fun sample2() {
        val actual = Solution().solution(
            intArrayOf(95, 90, 99, 99, 80, 99),
            intArrayOf(1, 1, 1, 1, 1, 1)
        )
        assertArrayEquals(intArrayOf(1, 3, 2), actual)
    }
}
