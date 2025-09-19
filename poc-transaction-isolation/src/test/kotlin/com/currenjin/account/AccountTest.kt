package com.currenjin.account

import com.currenjin.account.domain.Account
import com.currenjin.account.repository.AccountRepository
import com.currenjin.account.service.AccountService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

@SpringBootTest
class AccountTest(
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val accountService: AccountService,
) {
    @Test
    fun dirty_read_occurs_in_read_uncommitted() {
        val account = accountRepository.save(Account(balance = 100))

        val latch1 = CountDownLatch(1)
        val latch2 = CountDownLatch(1)

        val t1 =
            thread {
                accountService.updateWithoutCommit(account.id!!, 200, latch1, latch2)
            }

        val t2 =
            thread {
                latch1.await()
                val readBalance = accountService.readBalance(account.id!!)

                // 이 시점에 dirty read가 발생하면 200이 반환됨
                assertEquals(100, readBalance, "Expected original value, but dirty read may show updated one!")
                latch2.countDown()
            }

        t1.join()
        t2.join()
    }

    @Test
    fun non_repeatable_read_occurs_at_read_committed() {
        val id = accountRepository.save(Account(100)).id!!
        val l1 = CountDownLatch(1)
        val l2 = CountDownLatch(1)
        val first = AtomicLong()
        val second = AtomicLong()

        val t1 = thread { accountService.readTwiceInOneTx(id, l1, l2, first, second) }
        val t2 =
            thread {
                l1.await()
                accountService.updateAndCommit(id, 200)
                l2.countDown()
            }

        t1.join()
        t2.join()

        assertEquals(first.get(), second.get(), "non-repeatable read observed")
    }
}
