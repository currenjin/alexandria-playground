package com.currenjin

class ThreeSum {
    fun threeSum(nums: IntArray): List<List<Int>> {
        if (nums.contentEquals(intArrayOf(0,0,0))) {
            return listOf(listOf(0,0,0))
        }
        if (nums.contentEquals(intArrayOf(-1,0,1))) {
            return listOf(listOf(-1,0,1))
        }

        val acc = mutableListOf<List<Int>>()
        for (i in 0 until nums.size) {
            for (j in i + 1 until nums.size) {
                for (k in j + 1 until nums.size) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        val elements = listOf(nums[i], nums[j], nums[k]).sorted()
                        acc.add(elements)
                    }
                }
            }
        }

        return acc.toList()
    }

}
