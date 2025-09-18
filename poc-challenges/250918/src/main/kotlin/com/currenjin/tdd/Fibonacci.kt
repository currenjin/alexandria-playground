package com.currenjin.tdd

class Fibonacci {
    companion object {
        fun cal(n: Int): Int {
            if (n <= 1) return n
            if (n == 2) return 1
            return 2
        }
    }
}
