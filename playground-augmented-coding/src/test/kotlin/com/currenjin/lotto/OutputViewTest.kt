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
}
