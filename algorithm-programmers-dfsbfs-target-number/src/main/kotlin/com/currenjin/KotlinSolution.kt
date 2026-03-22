package com.currenjin

class KotlinSolution {
    fun solution(numbers: IntArray, target: Int): Int = dfs(numbers, 0, 0, target)

    private fun dfs(numbers: IntArray, idx: Int, sum: Int, target: Int): Int {
        if (idx == numbers.size) return if (sum == target) 1 else 0
        return dfs(numbers, idx + 1, sum + numbers[idx], target) +
            dfs(numbers, idx + 1, sum - numbers[idx], target)
    }
}
