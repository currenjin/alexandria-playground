package com.currenjin

class Solution {
    fun solution(participant: Array<String>, completion: Array<String>): String {
        val counts = HashMap<String, Int>()
        participant.forEach { counts[it] = (counts[it] ?: 0) + 1 }
        completion.forEach { name -> counts[name] = (counts[name] ?: 0) - 1 }
        return counts.entries.first { it.value > 0 }.key
    }
}
