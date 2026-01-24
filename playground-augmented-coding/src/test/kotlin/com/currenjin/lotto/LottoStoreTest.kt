package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class LottoStoreTest {

    @Test
    fun `should accept purchase amount in 1000 won units`() {
        val store = LottoStore()

        val lottos = store.purchase(1000)

        assertThat(lottos).hasSize(1)
    }

    @Test
    fun `should throw exception when amount is less than 1000`() {
        val store = LottoStore()

        assertThatThrownBy { store.purchase(999) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
