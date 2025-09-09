package com.currenjin.tdd

enum class OperationSymbol(
    val operator: String,
) {
    ADDITION_SYMBOL("+"),
    SUBTRACTION_SYMBOL("-"),
    MULTIPLICATION_SYMBOL("*"),
    DIVISION_SYMBOL("/"),
    ;

    companion object {
        fun isOperationSymbol(value: String): Boolean = entries.any { it.operator == value }
    }
}
