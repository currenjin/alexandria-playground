package com.currenjin.tdd

class Factorial {
    companion object {
        fun fac(n: Int): Int {
            if (n <= 2) return n
            return n * fac(n - 1)
        }
    }
}
