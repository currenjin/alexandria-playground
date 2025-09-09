package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class PostfixNotationTest {
    @ParameterizedTest
    @CsvSource("10, 6 4 +")
    fun postfixTest(expected: Int, postfix: String) {
        val actual = Postfix.calculate(postfix)

        assertEquals(expected, actual)
    }
}