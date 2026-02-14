package com.currenjin.seata.account

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val jdbcTemplate: JdbcTemplate,
) {
    @Transactional
    fun decreaseBalance(
        accountId: Long,
        amount: Long,
    ) {
        val updated =
            jdbcTemplate.update(
                """
                UPDATE account
                SET balance = balance - ?
                WHERE id = ? AND balance >= ?
                """.trimIndent(),
                amount,
                accountId,
                amount,
            )

        if (updated == 0) {
            throw IllegalStateException("잔액이 부족합니다. accountId=$accountId, amount=$amount")
        }
    }
}
