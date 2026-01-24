package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MainTest {

    @Test
    fun `should run main function`() {
        val input = ByteArrayInputStream("1000\n1, 2, 3, 4, 5, 6\n7\n".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        main()

        val result = output.toString()
        assertThat(result).contains("구입금액을 입력해 주세요.")
        assertThat(result).contains("당첨 통계")
    }
}
