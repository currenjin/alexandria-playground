package com.currenjin.tdd

class Fibonacci {
    companion object {
        fun fib(n: Int): Int {
            if (n == 0) return 0
            if (n <= 2) return 1
            return fib(n - 2) + fib(n - 1)
        }
    }

}
