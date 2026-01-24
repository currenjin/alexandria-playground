package com.currenjin.lotto

object OutputView {
    fun printLottos(lottos: List<Lotto>) {
        println("${lottos.size}개를 구매했습니다.")
        lottos.forEach { lotto ->
            println(lotto.numbers.map { it.value }.sorted())
        }
    }

    fun printStatistics(result: LottoResult) {
        println("당첨 통계")
        println("---------")
        println("3개 일치 (5000원)- ${result.countByRank(Rank.FIFTH)}개")
        println("4개 일치 (50000원)- ${result.countByRank(Rank.FOURTH)}개")
        println("5개 일치 (1500000원)- ${result.countByRank(Rank.THIRD)}개")
        println("5개 일치, 보너스 볼 일치(30000000원) - ${result.countByRank(Rank.SECOND)}개")
        println("6개 일치 (2000000000원)- ${result.countByRank(Rank.FIRST)}개")
    }
}
