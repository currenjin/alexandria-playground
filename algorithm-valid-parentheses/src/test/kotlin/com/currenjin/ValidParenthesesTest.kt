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

    @Test
    fun nested_returns_true() {
        val actual = ValidParentheses.isValid("{{}}")

        assertTrue(actual)
    }

    @Test
    fun interleaved_returns_true() {
        val actual = ValidParentheses.isValid("{{<({<<>>})>}}")

        assertTrue(actual)
    }

    @Test
    fun interleaved_returns_false() {
        val actual1 = ValidParentheses.isValid("{{<({<<<}>>})>}}")
        val actual2 = ValidParentheses.isValid("{{<({<<}>>})>}}")
        val actual3 = ValidParentheses.isValid("{{<({<<{>>})>}}")

        assertFalse(actual1)
        assertFalse(actual2)
        assertFalse(actual3)
    }

    @Test
    fun multiple_types_in_sequence_returns_true() {
        val actual = ValidParentheses.isValid("[](){}")

        assertTrue(actual)
    }

    @Test
    fun nested_then_sequence_returns_true() {
        val actual = ValidParentheses.isValid("[{{<>}}]()")

        assertTrue(actual)
    }
}
