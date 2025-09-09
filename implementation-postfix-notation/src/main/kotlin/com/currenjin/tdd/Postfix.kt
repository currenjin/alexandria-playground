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
                if (!OperationSymbol.isOperationSymbol(operand)) {
                    val number =
                        operand.toIntOrNull()
                            ?: throw IllegalArgumentException("operands must have numeric or operation")

                    stack.push(number)
                } else {
                    val operand2 = stack.pop()
                    val operand1 = stack.pop()

                    val result =
                        when (operand) {
                            OperationSymbol.ADDITION_SYMBOL.operator -> operand1 + operand2
                            OperationSymbol.SUBTRACTION_SYMBOL.operator -> operand1 - operand2
                            OperationSymbol.MULTIPLICATION_SYMBOL.operator -> operand1 * operand2
                            OperationSymbol.DIVISION_SYMBOL.operator -> operand1 / operand2
                            else -> throw IllegalArgumentException("operator must have an operational symbol")
                        }

                    stack.push(result)
                }
            }

            return stack.pop()
        }
    }
}
