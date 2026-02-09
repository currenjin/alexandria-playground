package com.currenjin.saga.core

sealed class SagaResult {
    data class Success(val message: String) : SagaResult()
    data class Failure(val reason: String, val retryable: Boolean = false) : SagaResult()
}
