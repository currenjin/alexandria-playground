package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SolutionTest {
    @Test
    fun sample1() {
        val maps = arrayOf(
            intArrayOf(1, 0, 1, 1, 1),
            intArrayOf(1, 0, 1, 0, 1),
            intArrayOf(1, 0, 1, 1, 1),
            intArrayOf(1, 1, 1, 0, 1),
            intArrayOf(0, 0, 0, 0, 1)
        )
        assertEquals(11, Solution().solution(maps))
    }

    @Test
    fun sample2() {
        val maps = arrayOf(
            intArrayOf(1, 0, 1, 1, 1),
            intArrayOf(1, 0, 1, 0, 1),
            intArrayOf(1, 0, 1, 1, 1),
            intArrayOf(1, 1, 1, 0, 0),
            intArrayOf(0, 0, 0, 0, 1)
        )
        assertEquals(-1, Solution().solution(maps))
    }
}
