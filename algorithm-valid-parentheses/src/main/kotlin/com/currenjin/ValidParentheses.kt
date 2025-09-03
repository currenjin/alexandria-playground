package com.currenjin

object ValidParentheses {
    fun isValid(s: String): Boolean {
        if (s.isBlank()) return true

        if (s.length == 2) return true

        return false
    }
}
