package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ThreeSumTest {
    @Test
    fun one_triplet_basic() {
        val nums = intArrayOf(0, 0, 0)

        val actual = ThreeSum().threeSum(nums)

        assertEquals(listOf(listOf(0, 0, 0)), actual)
    }
}