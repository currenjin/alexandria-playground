package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class LottoNumberTest {

    @Test
    fun `should create LottoNumber with valid number in range 1 to 45`() {
        val lottoNumber = LottoNumber(1)

        assertThat(lottoNumber.value).isEqualTo(1)
    }

    @Test
    fun `should throw exception when number is zero or less`() {
        assertThatThrownBy { LottoNumber(0) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should throw exception when number is 46 or more`() {
        assertThatThrownBy { LottoNumber(46) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
