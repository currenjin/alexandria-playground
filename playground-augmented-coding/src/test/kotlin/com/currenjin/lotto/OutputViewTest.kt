package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class OutputViewTest {

    @Test
    fun `should print purchased lottos`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val lottos = listOf(
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 6)),
            LottoGenerator.create(listOf(7, 8, 9, 10, 11, 12))
        )

        OutputView.printLottos(lottos)

        val result = output.toString()
        assertThat(result).contains("2개를 구매했습니다.")
        assertThat(result).contains("[1, 2, 3, 4, 5, 6]")
        assertThat(result).contains("[7, 8, 9, 10, 11, 12]")
    }

    @Test
    fun `should print winning statistics`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val lottos = listOf(
            LottoGenerator.create(listOf(1, 2, 3, 43, 44, 45)) // FIFTH
        )
        val winningLotto = WinningLotto(
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 6)),
            LottoNumber(7)
        )
        val lottoResult = LottoResult(lottos, winningLotto)

        OutputView.printStatistics(lottoResult)

        val result = output.toString()
        assertThat(result).contains("3개 일치 (5000원)- 1개")
        assertThat(result).contains("4개 일치 (50000원)- 0개")
        assertThat(result).contains("5개 일치 (1500000원)- 0개")
        assertThat(result).contains("5개 일치, 보너스 볼 일치(30000000원) - 0개")
        assertThat(result).contains("6개 일치 (2000000000원)- 0개")
    }

    @Test
    fun `should print profit rate`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val lottos = listOf(
            LottoGenerator.create(listOf(1, 2, 3, 43, 44, 45)) // FIFTH = 5000원
        )
        val winningLotto = WinningLotto(
            LottoGenerator.create(listOf(1, 2, 3, 4, 5, 6)),
            LottoNumber(7)
        )
        val lottoResult = LottoResult(lottos, winningLotto)

        OutputView.printProfitRate(lottoResult)

        val result = output.toString()
        assertThat(result).contains("총 수익률은 500.0%입니다.")
    }
}
