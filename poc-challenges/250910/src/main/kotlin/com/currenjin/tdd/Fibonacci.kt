package com.currenjin.tdd

class Fibonacci() {
    companion object {
        fun fib(n: Int): Int {
            if (n <= 1) return n
            return fib(n - 2) + fib(n - 1)
        }
    }

}
