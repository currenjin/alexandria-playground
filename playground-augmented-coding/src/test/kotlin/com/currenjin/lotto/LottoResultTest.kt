package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LottoResultTest {

    @Test
    fun `should create result from lottos and winning lotto`() {
        val lottos = listOf(
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 6)),
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 7)),
            LottoGenerator.create(listOf(1, 2, 3, 4, 8, 9))
        )
        val winningLotto = WinningLotto(
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 6)),
            LottoNumber(7)
        )

        val result = LottoResult(lottos, winningLotto)

        assertThat(result.countByRank(Rank.FIRST)).isEqualTo(1)
        assertThat(result.countByRank(Rank.SECOND)).isEqualTo(1)
        assertThat(result.countByRank(Rank.FOURTH)).isEqualTo(1)
    }
}
