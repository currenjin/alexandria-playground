package com.currenjin.account.service

import com.currenjin.account.repository.AccountRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicLong

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    @PersistenceContext private val entityManager: EntityManager,
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

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    fun readTwiceInOneTx(
        id: Long,
        l1: CountDownLatch,
        l2: CountDownLatch,
        a1: AtomicLong,
        a2: AtomicLong,
    ) {
        val result1 =
            entityManager
                .createQuery(
                    "select a.balance from Account a where a.id = :id",
                    java.lang.Long::class.java,
                ).setParameter("id", id)
                .singleResult

        a1.set(result1.toLong())
        l1.countDown()
        l2.await()

        entityManager.clear()
        val result2 =
            entityManager
                .createQuery(
                    "select a.balance from Account a where a.id = :id",
                    java.lang.Long::class.java,
                ).setParameter("id", id)
                .singleResult
        a2.set(result2.toLong())
    }

    fun updateAndCommit(
        id: Long,
        v: Long,
    ) {
        val account = accountRepository.findById(id).orElseThrow()
        account.balance = v
    }
}
