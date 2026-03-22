package com.currenjin

class KotlinSolution {
    fun solution(participant: Array<String>, completion: Array<String>): String {
        val counts = HashMap<String, Int>()
        participant.forEach { counts[it] = (counts[it] ?: 0) + 1 }
        completion.forEach { counts[it] = (counts[it] ?: 0) - 1 }
        return counts.entries.first { it.value > 0 }.key
    }
}
