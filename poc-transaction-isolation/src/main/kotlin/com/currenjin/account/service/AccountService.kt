package com.currenjin.account.service

import com.currenjin.account.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicLong

@Service
class AccountService(
    private val accountRepository: AccountRepository,
) {
    @Transactional
    fun updateWithoutCommit(
        id: Long,
        newBalance: Long,
        latch1: CountDownLatch,
        latch2: CountDownLatch,
    ) {
        val account = accountRepository.findById(id).orElseThrow()
        account.balance = newBalance
        latch1.countDown()
        latch2.await()
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    fun readBalance(id: Long): Long = accountRepository.findById(id).orElseThrow().balance

    fun updateAndCommit(
        id: Long,
        i: Int,
    ) {
        TODO("Not yet implemented")
    }

    fun readTwiceInOneTx(
        id: Long,
        l1: CountDownLatch,
        l2: CountDownLatch,
        first: AtomicLong,
        second: AtomicLong,
    ) {
        TODO("Not yet implemented")
    }
}
