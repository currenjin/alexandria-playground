package com.currenjin.tdd

class Fibonacci {
    companion object {
        fun fib(number: Int): Int {
            if (number <= 1) return number
            if (number == 2) return 1
            return fib(number - 2) + fib(number - 1)
        }
    }
}
