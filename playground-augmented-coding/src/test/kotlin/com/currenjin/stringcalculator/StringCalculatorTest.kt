package com.currenjin.stringcalculator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringCalculatorTest {
    @Test
    fun `빈 문자열은 0을 반환한다`() {
        val calculator = StringCalculator()
        assertEquals(0, calculator.add(""))
    }

    @Test
    fun `숫자 하나는 해당 숫자를 반환한다`() {
        val calculator = StringCalculator()
        assertEquals(1, calculator.add("1"))
    }

    @Test
    fun `쉼표로 구분된 두 숫자는 합계를 반환한다`() {
        val calculator = StringCalculator()
        assertEquals(3, calculator.add("1,2"))
    }
}