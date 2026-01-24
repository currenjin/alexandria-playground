package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class LottoTest {

    @Test
    fun `should create Lotto with 6 LottoNumbers`() {
        val lotto = Lotto(
            listOf(
                LottoNumber(1),
                LottoNumber(2),
                LottoNumber(3),
                LottoNumber(4),
                LottoNumber(5),
                LottoNumber(6)
            )
        )

        assertThat(lotto.numbers).hasSize(6)
    }

    @Test
    fun `should throw exception when less than 6 numbers`() {
        assertThatThrownBy {
            Lotto(
                listOf(
                    LottoNumber(1),
                    LottoNumber(2),
                    LottoNumber(3),
                    LottoNumber(4),
                    LottoNumber(5)
                )
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should throw exception when more than 6 numbers`() {
        assertThatThrownBy {
            Lotto(
                listOf(
                    LottoNumber(1),
                    LottoNumber(2),
                    LottoNumber(3),
                    LottoNumber(4),
                    LottoNumber(5),
                    LottoNumber(6),
                    LottoNumber(7)
                )
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should throw exception when numbers contain duplicates`() {
        assertThatThrownBy {
            Lotto(
                listOf(
                    LottoNumber(1),
                    LottoNumber(2),
                    LottoNumber(3),
                    LottoNumber(4),
                    LottoNumber(5),
                    LottoNumber(5)
                )
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should return true when lotto contains the number`() {
        val lotto = Lotto(
            listOf(
                LottoNumber(1),
                LottoNumber(2),
                LottoNumber(3),
                LottoNumber(4),
                LottoNumber(5),
                LottoNumber(6)
            )
        )

        assertThat(lotto.contains(LottoNumber(3))).isTrue()
    }
}
