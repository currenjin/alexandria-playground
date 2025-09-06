package com.currenjin.tdd

class Factorial() {
    companion object {
        fun fac(n: Int): Int {
            if (n <= 2) return n
            return 2 * n
        }
    }

}
