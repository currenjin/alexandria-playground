package com.currenjin

class Nums {
    companion object {
        fun twoSum(nums: ArrayList<Int>, target: Int): ArrayList<Int> {
            val result = arrayListOf<Int>()

            for (i in 0 until nums.size) {
                for (j in 0 until nums.size) {
                    if (nums[i] + nums[j] == target && i != j) {
                        result.add(i)
                        result.add(j)
                        return result
                    }
                }
            }

            return result
        }
    }

}
