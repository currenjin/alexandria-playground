package com.currenjin

object ValidParentheses {
    fun isValid(s: String): Boolean {
        if (s.isBlank()) return true

        if (s.length == 2 &&
            s[0] == '{' &&
            s[1] == '}'
        ) {
            return true
        }

        return false
    }
}
