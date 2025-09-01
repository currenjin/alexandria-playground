package com.currenjin

class ThreeSum {
    fun threeSum(nums: IntArray): List<List<Int>> {
        val acc = mutableSetOf<List<Int>>()
        for (i in 0 until nums.size) {
            for (j in i + 1 until nums.size) {
                for (k in j + 1 until nums.size) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        acc += listOf(nums[i], nums[j], nums[k]).sorted()
                    }
                }
            }
        }

        return acc.toList().sortedWith(
            compareBy({ it[0] }, { it[1] }, { it[2] }),
        )
    }
}
