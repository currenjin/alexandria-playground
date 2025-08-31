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

    @Test
    fun _2_3_4_targetIs6_returns1And3() {
        val nums = arrayOf(2, 3, 4)

        val actual = Nums.twoSum(nums, 6)

        assertEquals(arrayListOf(1, 3), actual)
    }

    @Test
    fun _minus1_0_targetIsMinus1_returns1And2() {
        val nums = arrayOf(-1, 0)

        val actual = Nums.twoSum(nums, -1)

        assertEquals(arrayListOf(1, 2), actual)
    }
}