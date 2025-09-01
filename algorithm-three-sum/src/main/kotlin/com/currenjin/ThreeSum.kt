package com.currenjin

class ThreeSum {
    fun threeSum(nums: IntArray): List<List<Int>> {
        val numList = nums.sorted()
        val result = mutableListOf<List<Int>>()
        for (i in 0 until numList.size - 2) {
            if (i > 0 && numList[i] == numList[i - 1]) continue
            var l = i + 1
            var r = numList.lastIndex
            while (l < r) {
                val s = numList[i] + numList[l] + numList[r]
                when {
                    s == 0 -> {
                        result += listOf(numList[i], numList[l], numList[r])
                        l++
                        r--
                        while (l < r && numList[l] == numList[l - 1]) l++
                        while (l < r && numList[r] == numList[r + 1]) r--
                    }
                    s < 0 -> l++
                    else -> r--
                }
            }
        }
        return result
    }
}
