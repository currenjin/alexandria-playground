package com.currenjin

class KotlinSolution {
    fun solution(clothes: Array<Array<String>>): Int {
        val countByKind = HashMap<String, Int>()
        clothes.forEach { (_, kind) -> countByKind[kind] = (countByKind[kind] ?: 0) + 1 }
        return countByKind.values.fold(1) { acc, c -> acc * (c + 1) } - 1
    }
}
