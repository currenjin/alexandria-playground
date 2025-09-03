package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ValidParenthesesTest {
    @Test
    fun empty_returnsTrue() {
        val actual = ValidParentheses.isValid("")

        assertTrue(actual)
    }
}