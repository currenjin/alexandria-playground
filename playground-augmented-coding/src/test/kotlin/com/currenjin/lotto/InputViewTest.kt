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
}
