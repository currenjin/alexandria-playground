package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LottoStoreTest {

    @Test
    fun `should accept purchase amount in 1000 won units`() {
        val store = LottoStore()

        val lottos = store.purchase(1000)

        assertThat(lottos).hasSize(1)
    }
}
