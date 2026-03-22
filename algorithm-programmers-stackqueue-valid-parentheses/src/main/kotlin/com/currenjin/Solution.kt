package com.currenjin

class Solution {
    fun solution(s: String): Boolean {
        var balance = 0
        for (ch in s) {
            if (ch == '(') balance++ else balance--
            if (balance < 0) return false
        }
        return balance == 0
    }
}
