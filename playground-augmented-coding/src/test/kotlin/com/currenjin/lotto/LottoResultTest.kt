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

    @Test
    fun `should count each rank correctly`() {
        val lottos = listOf(
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 6)),  // FIRST
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 7)),  // SECOND (bonus)
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 45)), // THIRD
            LottoGenerator.create(listOf(1, 2, 3, 4, 44, 45)),// FOURTH
            LottoGenerator.create(listOf(1, 2, 3, 43, 44, 45)),// FIFTH
            LottoGenerator.create(listOf(1, 2, 42, 43, 44, 45)) // MISS
        )
        val winningLotto = WinningLotto(
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 6)),
            LottoNumber(7)
        )

        val result = LottoResult(lottos, winningLotto)

        assertThat(result.countByRank(Rank.FIRST)).isEqualTo(1)
        assertThat(result.countByRank(Rank.SECOND)).isEqualTo(1)
        assertThat(result.countByRank(Rank.THIRD)).isEqualTo(1)
        assertThat(result.countByRank(Rank.FOURTH)).isEqualTo(1)
        assertThat(result.countByRank(Rank.FIFTH)).isEqualTo(1)
        assertThat(result.countByRank(Rank.MISS)).isEqualTo(1)
    }

    @Test
    fun `should calculate profit rate`() {
        val lottos = listOf(
            LottoGenerator.create(listOf(1, 2, 3, 43, 44, 45)) // FIFTH = 5,000원
        )
        val winningLotto = WinningLotto(
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 6)),
            LottoNumber(7)
        )

        val result = LottoResult(lottos, winningLotto)

        // 수익률 = (5,000 / 1,000) * 100 = 500%
        assertThat(result.profitRate()).isEqualTo(500.0)
    }
}
