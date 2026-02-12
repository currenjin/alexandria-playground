package com.currenjin.idempotency.core

import java.util.UUID

data class IdempotencyKey(val value: String) {
    init {
        require(value.isNotBlank()) { "멱등성 키는 비어있을 수 없습니다" }
    }

    companion object {
        fun generate(): IdempotencyKey = IdempotencyKey(UUID.randomUUID().toString())

        fun from(vararg parts: String): IdempotencyKey =
            IdempotencyKey(parts.joinToString(":"))
    }
}
