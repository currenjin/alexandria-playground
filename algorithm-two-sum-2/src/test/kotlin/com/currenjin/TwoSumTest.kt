package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwoSumTest {
    @Test
    fun _2_7_11_15_targetIs9_returns1And2() {
        val nums = arrayOf(2, 7, 11, 15)

        val actual = Nums.twoSum(nums, 9)

        assertEquals(arrayListOf(1, 2), actual)
    }
}