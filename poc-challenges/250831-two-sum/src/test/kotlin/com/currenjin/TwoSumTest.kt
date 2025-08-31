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

    @Test
    fun _3_2_4_target_6_should_return_1_2() {
        val nums = arrayListOf(3, 2, 4)

        val actual = Nums.twoSum(nums, 6)

        assertEquals(arrayListOf(1, 2), actual)
    }
}