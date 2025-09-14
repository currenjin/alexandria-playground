package com.currenjin.tdd

class Factorial {
    companion object {
        fun cal(n: Int): Int {
            if (n <= 2) return n
            return n * cal(n - 1)
        }
    }
}
