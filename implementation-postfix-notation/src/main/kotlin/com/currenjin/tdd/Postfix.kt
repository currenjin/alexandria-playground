package com.currenjin.tdd

import java.util.Stack

class Postfix() {
    companion object {
        fun calculate(stack: Stack<String>): Int {
            throw IllegalArgumentException("Stack must have operands to perform calculation")
        }
    }

}
