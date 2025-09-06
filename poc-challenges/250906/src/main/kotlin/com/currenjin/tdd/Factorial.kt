package com.currenjin.tdd

class Factorial() {
    companion object {
        fun fac(n: Int): Int {
            if (n <= 2) return n
            return (n - 2) * (n - 1) * n
        }
    }

}
