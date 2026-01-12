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

    @Test
    fun `예외 메시지에 모든 음수 값이 포함된다`() {
        val calculator = StringCalculator()
        val exception = assertThrows<IllegalArgumentException> {
            calculator.add("-1,2,-3")
        }
        assertEquals("negatives not allowed: -1, -3", exception.message)
    }

    @Test
    fun `1000 초과 숫자는 무시한다`() {
        val calculator = StringCalculator()
        assertEquals(2, calculator.add("2,1001"))
    }

    @Test
    fun `임의 길이 커스텀 구분자를 지원한다`() {
        val calculator = StringCalculator()
        assertEquals(6, calculator.add("//[***]\n1***2***3"))
    }

    @Test
    fun `다중 구분자를 지원한다`() {
        val calculator = StringCalculator()
        assertEquals(6, calculator.add("//[*][%]\n1*2%3"))
    }
}