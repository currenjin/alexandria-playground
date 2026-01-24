package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class InputViewTest {

    @Test
    fun `should read purchase amount from input`() {
        val input = ByteArrayInputStream("14000\n".toByteArray())
        System.setIn(input)

        val amount = InputView.readPurchaseAmount()

        assertThat(amount).isEqualTo(14000)
    }

    @Test
    fun `should read winning numbers from comma separated input`() {
        val input = ByteArrayInputStream("1, 2, 3, 4, 5, 6\n".toByteArray())
        System.setIn(input)

        val numbers = InputView.readWinningNumbers()

        assertThat(numbers).containsExactly(1, 2, 3, 4, 5, 6)
    }

    @Test
    fun `should read bonus number from input`() {
        val input = ByteArrayInputStream("7\n".toByteArray())
        System.setIn(input)

        val bonus = InputView.readBonusNumber()

        assertThat(bonus).isEqualTo(7)
    }
}
