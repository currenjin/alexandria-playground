package com.currenjin

class Solution {
    fun solution(progresses: IntArray, speeds: IntArray): IntArray {
        val days = progresses.indices.map { idx ->
            val remain = 100 - progresses[idx]
            (remain + speeds[idx] - 1) / speeds[idx]
        }

        val result = mutableListOf<Int>()
        var current = days.first()
        var count = 1

        for (i in 1 until days.size) {
            if (days[i] <= current) {
                count++
            } else {
                result.add(count)
                current = days[i]
                count = 1
            }
        }
        result.add(count)
        return result.toIntArray()
    }
}
