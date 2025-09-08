package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.Stack

class PostfixNotationTest {
    @Test
    fun throwsException_whenStackDoesNotHaveOperands() {
        val stack = Stack<String>()

        stack.push("10")
        stack.push("20")

        assertThrows(IllegalArgumentException::class.java) { Postfix.calculate(stack) }
    }
}