package com.currenjin.tdd

class Fibonacci() {
    companion object {
        fun fibonacci(n: Int): Int {
            if (n == 0) return 0
            if (n <= 2) return 1
            return fibonacci(n - 2) + fibonacci(n - 1)
        }
    }

}
