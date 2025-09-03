package com.currenjin

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidParenthesesTest {
    @Test
    fun empty_returnsTrue() {
        val actual = ValidParentheses.isValid("")

        assertTrue(actual)
    }

    @Test
    fun single_open_returnsFalse() {
        val actual = ValidParentheses.isValid("{")

        assertFalse(actual)
    }

    @Test
    fun single_pair_returnsTrue() {
        val actual = ValidParentheses.isValid("{}")

        assertTrue(actual)
    }
}
