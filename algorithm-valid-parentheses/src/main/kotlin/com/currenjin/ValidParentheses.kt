package com.currenjin

object ValidParentheses {
    fun isValid(s: String): Boolean {
        if (s.isBlank()) return true

        if (s.length % 2 != 0) return false

        val mid = s.length / 2
        val left = s.substring(0, mid)
        val right = s.substring(mid)
        val reverseRight =
            right
                .reversed()
                .map { it.toReverseParentheses() }
                .joinToString("")

        return left == reverseRight
    }
}

private fun Char.toReverseParentheses() =
    when (this) {
        '}' -> '{'
        ']' -> '['
        '>' -> '<'
        else -> ' '
    }
