package com.currenjin.tdd

class Fibonacci() {
    companion object {
        fun fibonacci(n: Int): Int {
            if (n <= 1) return n
            return fibonacci(n - 2) + fibonacci(n - 1)
        }
    }

}
