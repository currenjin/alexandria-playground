package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class PostfixNotationTest {
    @ParameterizedTest
    @CsvSource(
        "10, 6 4 +",
        "15, 10 5 +",
        "15, 5 3 *",
        "11, 5 3 2 * +",
    )
    fun postfixTest(
        expected: Int,
        postfix: String,
    ) {
        val actual = Postfix.calculate(postfix)

        assertEquals(expected, actual)
    }

    @Test
    fun throwsException_stringIsBlank() {
        assertThrows<IllegalArgumentException> { Postfix.calculate(" ") }
    }
}
