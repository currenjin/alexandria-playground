package com.currenjin.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RankTest {

    @Test
    fun `should return FIRST rank with prize 2_000_000_000 when 6 numbers match`() {
        val rank = Rank.FIRST

        assertThat(rank.matchCount).isEqualTo(6)
        assertThat(rank.prize).isEqualTo(2_000_000_000)
    }
}
