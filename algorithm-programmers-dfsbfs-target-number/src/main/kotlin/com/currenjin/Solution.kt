package com.currenjin

class Solution {
    fun solution(numbers: IntArray, target: Int): Int {
        return dfs(numbers, 0, 0, target)
    }

    private fun dfs(numbers: IntArray, idx: Int, sum: Int, target: Int): Int {
        if (idx == numbers.size) return if (sum == target) 1 else 0
        val plus = dfs(numbers, idx + 1, sum + numbers[idx], target)
        val minus = dfs(numbers, idx + 1, sum - numbers[idx], target)
        return plus + minus
    }
}
