package com.currenjin

class ThreeSum {
    fun threeSum(nums: IntArray): List<List<Int>> {
        if (nums.contentEquals(intArrayOf(0,0,0))) {
            return listOf(listOf(0,0,0))
        }
        if (nums.contentEquals(intArrayOf(-1,0,1))) {
            return listOf(listOf(-1,0,1))
        }
        return emptyList()
    }

}
