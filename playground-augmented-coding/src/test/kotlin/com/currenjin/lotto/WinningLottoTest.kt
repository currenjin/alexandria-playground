package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WinningLottoTest {

    @Test
    fun `should create WinningLotto with 6 numbers and bonus number`() {
        val lotto = Lotto(listOf(
            LottoNumber(1),
            LottoNumber(2),
            LottoNumber(3),
            LottoNumber(4),
            LottoNumber(5),
            LottoNumber(6)
        ))
        val bonus = LottoNumber(7)

        val winningLotto = WinningLotto(lotto, bonus)

        assertThat(winningLotto.lotto).isEqualTo(lotto)
        assertThat(winningLotto.bonus).isEqualTo(bonus)
    }
}
