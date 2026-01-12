package com.currenjin.stringcalculator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringCalculatorTest {
    @Test
    fun `빈 문자열은 0을 반환한다`() {
        val calculator = StringCalculator()
        assertEquals(0, calculator.add(""))
    }
}