package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ThreeSumTest {
    @Test
    fun threeZeros_returnsSingleTriplet() {
        val nums = intArrayOf(0, 0, 0)

        val actual = ThreeSum().threeSum(nums)

        assertEquals(listOf(listOf(0, 0, 0)), actual)
    }

    @Test
    fun noTriplet_returnsEmpty() {
        val nums = intArrayOf(0, 1, 1)

        val actual = ThreeSum().threeSum(nums)

        assertEquals(emptyList<List<Int>>(), actual)
    }

    @Test
    fun arrayWithNegativeOneZeroAndOne_returnsTriplet() {
        val nums = intArrayOf(-1, 0, 1)

        val actual = ThreeSum().threeSum(nums)

        assertEquals(listOf(listOf(-1, 0, 1)), actual)
    }

    @Test
    fun arrayWithTwoNegativesAndOnePositive_returnsTriplet() {
        val nums = intArrayOf(-1, -1, 2)

        val actual = ThreeSum().threeSum(nums)

        assertEquals(listOf(listOf(-1, -1, 2)), actual)
    }

    @Test
    fun arrayWithDuplicateTriplets_returnsDistinctTriplets() {
        val nums = intArrayOf(-1, 0, 1, -1)

        val actual = ThreeSum().threeSum(nums)

        assertEquals(listOf(listOf(-1, 0, 1)), actual)
    }

    @Test
    fun arrayWithMixedNumbers_returnsAllDistinctTriplets() {
        val nums = intArrayOf(-1, 0, 1, 2, -1, -4)

        val actual = ThreeSum().threeSum(nums)

        assertEquals(listOf(listOf(-1, -1, 2), listOf(-1, 0, 1)), actual)
    }
}
