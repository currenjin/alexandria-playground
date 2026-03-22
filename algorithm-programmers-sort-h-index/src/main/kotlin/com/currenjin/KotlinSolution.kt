package com.currenjin

class KotlinSolution {
    fun solution(citations: IntArray): Int {
        val sorted = citations.sortedDescending()
        var h = 0
        for (i in sorted.indices) {
            val candidate = i + 1
            if (sorted[i] >= candidate) h = candidate else break
        }
        return h
    }
}
