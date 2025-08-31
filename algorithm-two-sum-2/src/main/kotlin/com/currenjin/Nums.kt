package com.currenjin

class Nums {
    companion object {
        fun twoSum(nums: Array<Int>, target: Int): ArrayList<Int> {
            val result = arrayListOf<Int>()

            for (i in nums.indices ) {
                for (j in nums.indices)
                if (nums[i] + nums[j] == target && i != j) {
                    result.add(i + 1)
                    result.add(j + 1)
                    return result
                }
            }

            return result
        }
    }

}
