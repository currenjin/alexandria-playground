package com.currenjin.account

import com.currenjin.account.domain.Account
import com.currenjin.account.repository.AccountRepository
import com.currenjin.account.service.AccountService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread
import kotlin.test.assertEquals

@SpringBootTest
class DirtyReadTest(
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
}
