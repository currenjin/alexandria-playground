package com.currenjin.tdd

import java.util.Stack

class Postfix {
    companion object {
        fun calculate(postfix: String): Int {
            val operands = postfix.split(" ")

            if (postfix.isEmpty()) {
                throw IllegalArgumentException("postfix must not be blank")
            }

            if (operands.size <= 2) {
                throw IllegalArgumentException("operands must have at least 2 operands")
            }

            val stack = Stack<Int>()

            for (operand in operands) {
                val number = operand.toIntOrNull()
                if (number != null) {
                    stack.push(number)
                } else {
                    val operand2 = stack.pop()
                    val operand1 = stack.pop()

                    val result =
                        when (operand) {
                            "+" -> operand1 + operand2
                            "-" -> operand1 - operand2
                            "*" -> operand1 * operand2
                            "/" -> operand1 / operand2
                            else -> operands.last().toInt()
                        }

                    stack.push(result)
                }
            }

            return stack.pop()
        }
    }
}
