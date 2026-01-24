package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LottoNumberTest {

    @Test
    fun `should create LottoNumber with valid number in range 1 to 45`() {
        val lottoNumber = LottoNumber(1)

        assertThat(lottoNumber.value).isEqualTo(1)
    }
}
