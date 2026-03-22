package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SolutionTest {
    @Test
    fun sample1() {
        val clothes = arrayOf(
            arrayOf("yellow_hat", "headgear"),
            arrayOf("blue_sunglasses", "eyewear"),
            arrayOf("green_turban", "headgear")
        )
        assertEquals(5, Solution().solution(clothes))
    }

    @Test
    fun sample2() {
        val clothes = arrayOf(
            arrayOf("crow_mask", "face"),
            arrayOf("blue_sunglasses", "face"),
            arrayOf("smoky_makeup", "face")
        )
        assertEquals(3, Solution().solution(clothes))
    }
}
