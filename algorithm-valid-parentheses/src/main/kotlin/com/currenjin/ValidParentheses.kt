package com.currenjin

object ValidParentheses {
    private val open = setOf('(', '{', '[', '<')
    private val match = mapOf(')' to '(', '}' to '{', ']' to '[', '>' to '<')

    fun isValid(s: String): Boolean {
        if (s.isBlank()) return true
        if (s.length % 2 != 0) return false

        val arrayOfString = ArrayDeque<Char>()
        for (char in s) {
            when (char) {
                in open -> arrayOfString.addLast(char)
                in match -> if (arrayOfString.isEmpty() || arrayOfString.removeLast() != match[char]) return false
                else -> return false
            }
        }
        return arrayOfString.isEmpty()
    }
}
