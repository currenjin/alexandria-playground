package com.currenjin.tdd

class Factorial() {
    companion object {
        fun fac(n: Int): Int {
            if (n == 1) return 1
            return fac(n - 1) * n
        }
    }

}
