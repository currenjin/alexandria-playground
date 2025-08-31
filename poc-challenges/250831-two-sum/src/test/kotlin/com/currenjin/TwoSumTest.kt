package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwoSumTest {
    @Test
    fun _2_7_11_15_target_9_should_return_0_1() {
        val nums = arrayListOf(2, 7, 11, 15)

        val actual = Nums.twoSum(nums, 9)

        assertEquals(arrayListOf(0, 1), actual)
    }
}