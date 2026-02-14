package com.currenjin.seata.order

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OrderRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    @Transactional
    fun create(
        userId: Long,
        accountId: Long,
        amount: Long,
    ): Long {
        jdbcTemplate.update(
            """
            INSERT INTO purchase_order (user_id, account_id, amount, status)
            VALUES (?, ?, ?, 'CREATED')
            """.trimIndent(),
            userId,
            accountId,
            amount,
        )

        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long::class.java)
            ?: throw IllegalStateException("주문 ID를 생성하지 못했습니다.")
    }
}
