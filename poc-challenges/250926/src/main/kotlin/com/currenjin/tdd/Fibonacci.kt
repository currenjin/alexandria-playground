package com.currenjin.tdd

class Fibonacci {
    companion object {
        fun fib(number: Int): Int {
            if (number <= 1) return number
            return 1
        }
    }
}
