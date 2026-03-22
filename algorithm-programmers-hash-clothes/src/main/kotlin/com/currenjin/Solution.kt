package com.currenjin

class Solution {
    fun solution(clothes: Array<Array<String>>): Int {
        val kindCount = HashMap<String, Int>()
        clothes.forEach { (_, kind) -> kindCount[kind] = (kindCount[kind] ?: 0) + 1 }

        var answer = 1
        for (count in kindCount.values) {
            answer *= (count + 1)
        }
        return answer - 1
    }
}
