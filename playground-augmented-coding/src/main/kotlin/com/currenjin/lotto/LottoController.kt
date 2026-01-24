package com.currenjin.lotto

object LottoController {
    fun run() {
        val amount = InputView.readPurchaseAmount()
        val store = LottoStore()
        val lottos = store.purchase(amount)
        OutputView.printLottos(lottos)

        val winningNumbers = InputView.readWinningNumbers()
        val bonusNumber = InputView.readBonusNumber()

        val winningLotto = WinningLotto(
            LottoGenerator.create(winningNumbers),
            LottoNumber(bonusNumber)
        )

        val result = LottoResult(lottos, winningLotto)
        OutputView.printStatistics(result)
        OutputView.printProfitRate(result)
    }
}
