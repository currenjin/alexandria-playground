package com.currenjin.saga.core

interface SagaStep<T> {
    val name: String
    fun execute(context: T): SagaResult
    fun compensate(context: T): SagaResult
}
