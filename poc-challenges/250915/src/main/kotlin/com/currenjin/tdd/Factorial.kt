package com.currenjin.tdd

class Factorial {
    companion object {
        fun cal(n: Int): Int {
            if (n <= 2) return n
            return 3 * (n - 1)
        }
    }
}
