package com.currenjin.lotto

class LottoResult(
    private val lottos: List<Lotto>,
    private val winningLotto: WinningLotto
) {
    private val results: Map<Rank, Int> = lottos
        .map { lotto ->
            val matchCount = winningLotto.matchCount(lotto)
            val matchBonus = winningLotto.matchBonus(lotto)
            Rank.of(matchCount, matchBonus)
        }
        .groupingBy { it }
        .eachCount()

    fun countByRank(rank: Rank): Int = results[rank] ?: 0
}
