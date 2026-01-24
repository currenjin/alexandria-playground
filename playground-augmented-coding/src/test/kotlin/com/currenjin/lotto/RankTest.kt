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

    @Test
    fun `should return SECOND rank with prize 30_000_000 when 5 numbers match with bonus`() {
        val rank = Rank.SECOND

        assertThat(rank.matchCount).isEqualTo(5)
        assertThat(rank.prize).isEqualTo(30_000_000)
    }
}
