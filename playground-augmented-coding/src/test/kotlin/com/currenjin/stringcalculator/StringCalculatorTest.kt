package com.currenjin.stringcalculator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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

    @Test
    fun `임의 개수의 숫자는 합계를 반환한다`() {
        val calculator = StringCalculator()
        assertEquals(15, calculator.add("1,2,3,4,5"))
    }

    @Test
    fun `줄바꿈도 구분자로 허용한다`() {
        val calculator = StringCalculator()
        assertEquals(6, calculator.add("1\n2,3"))
    }

    @Test
    fun `음수가 포함되면 예외가 발생한다`() {
        val calculator = StringCalculator()
        assertThrows<IllegalArgumentException> {
            calculator.add("-1,2")
        }
    }
}