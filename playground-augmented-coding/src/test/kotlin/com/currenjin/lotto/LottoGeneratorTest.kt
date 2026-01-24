package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LottoGeneratorTest {

    @Test
    fun `should generate lotto with 6 random numbers between 1 and 45`() {
        val lotto = LottoGenerator.generate()

        assertThat(lotto.numbers).hasSize(6)
        assertThat(lotto.numbers.map { it.value }).allMatch { it in 1..45 }
    }

    @Test
    fun `should generate lotto with no duplicate numbers`() {
        val lotto = LottoGenerator.generate()

        assertThat(lotto.numbers.map { it.value }.distinct()).hasSize(6)
    }
}
