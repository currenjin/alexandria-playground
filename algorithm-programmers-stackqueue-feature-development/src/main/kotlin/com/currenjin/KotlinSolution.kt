package com.currenjin

class KotlinSolution {
    fun solution(progresses: IntArray, speeds: IntArray): IntArray {
        val days = progresses.indices.map { i ->
            val remain = 100 - progresses[i]
            (remain + speeds[i] - 1) / speeds[i]
        }
        val result = mutableListOf<Int>()
        var current = days.first()
        var count = 1
        for (i in 1 until days.size) {
            if (days[i] <= current) count++
            else {
                result += count
                current = days[i]
                count = 1
            }
        }
        result += count
        return result.toIntArray()
    }
}
