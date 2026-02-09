package com.currenjin.saga.core

data class RetryPolicy(
    val maxRetries: Int = 3,
    val delayMs: Long = 100,
)
