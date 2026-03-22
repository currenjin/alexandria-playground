package com.currenjin

class KotlinSolution {
    fun solution(n: Int, times: IntArray): Long {
        var left = 1L
        var right = times.max().toLong() * n
        var answer = right

        while (left <= right) {
            val mid = (left + right) / 2
            var processed = 0L
            for (t in times) processed += mid / t

            if (processed >= n) {
                answer = mid
                right = mid - 1
            } else {
                left = mid + 1
            }
        }
        return answer
    }
}
