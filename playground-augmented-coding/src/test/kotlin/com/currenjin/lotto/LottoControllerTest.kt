package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class LottoControllerTest {

    @Test
    fun `should run full game flow`() {
        val input = ByteArrayInputStream("1000\n1, 2, 3, 4, 5, 6\n7\n".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        LottoController.run()

        val result = output.toString()
        assertThat(result).contains("구입금액을 입력해 주세요.")
        assertThat(result).contains("1개를 구매했습니다.")
        assertThat(result).contains("지난 주 당첨 번호를 입력해 주세요.")
        assertThat(result).contains("보너스 볼을 입력해 주세요.")
        assertThat(result).contains("당첨 통계")
        assertThat(result).contains("총 수익률은")
    }
}
