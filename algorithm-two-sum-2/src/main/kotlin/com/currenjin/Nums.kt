package com.currenjin

class Nums {
    companion object {
        fun twoSum(nums: IntArray, target: Int): IntArray {
            var length = 0
            var r = nums.lastIndex

            while (length < r) {
                val sum = nums[length] + nums[r]
                when {
                    sum == target -> return intArrayOf(length + 1, r + 1)
                    sum < target -> length++
                    else -> r--
                }
            }

            return intArrayOf()
        }
    }

}
