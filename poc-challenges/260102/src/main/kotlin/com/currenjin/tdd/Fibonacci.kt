package com.currenjin.tdd

class Fibonacci {
    companion object {
        fun fib(i: Int): Int {
            if (i <= 2) return 1

            return fib(i - 1) + fib(i - 2)
        }
    }
}
