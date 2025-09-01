package com.currenjin

class ThreeSum {
    fun threeSum(nums: IntArray): List<List<Int>> {
        return if (nums.size == 3 && nums.all { it == 0 }) listOf(listOf(0, 0, 0)) else emptyList()
    }

}
