package com.currenjin.idempotency.core

sealed class IdempotencyResult<out T> {
    data class Executed<T>(val response: T) : IdempotencyResult<T>()
    data class Replayed<T>(val response: T) : IdempotencyResult<T>()
    data class Rejected(val reason: String) : IdempotencyResult<Nothing>()
}
