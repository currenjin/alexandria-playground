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

    @Test
    fun `should return THIRD rank with prize 1_500_000 when 5 numbers match`() {
        val rank = Rank.THIRD

        assertThat(rank.matchCount).isEqualTo(5)
        assertThat(rank.prize).isEqualTo(1_500_000)
    }

    @Test
    fun `should return FOURTH rank with prize 50_000 when 4 numbers match`() {
        val rank = Rank.FOURTH

        assertThat(rank.matchCount).isEqualTo(4)
        assertThat(rank.prize).isEqualTo(50_000)
    }

    @Test
    fun `should return FIFTH rank with prize 5_000 when 3 numbers match`() {
        val rank = Rank.FIFTH

        assertThat(rank.matchCount).isEqualTo(3)
        assertThat(rank.prize).isEqualTo(5_000)
    }

    @Test
    fun `should return MISS rank with prize 0 when 2 or less numbers match`() {
        val rank = Rank.MISS

        assertThat(rank.matchCount).isEqualTo(0)
        assertThat(rank.prize).isEqualTo(0)
    }
}
