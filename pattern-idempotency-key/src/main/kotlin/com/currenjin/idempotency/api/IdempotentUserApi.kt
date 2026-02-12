package com.currenjin.idempotency.api

import com.currenjin.idempotency.core.IdempotencyExecutor
import com.currenjin.idempotency.core.IdempotencyKey
import com.currenjin.idempotency.core.IdempotencyResult
import com.currenjin.idempotency.core.InMemoryIdempotencyStore

class IdempotentUserApi(
    private val repository: UserRepository = UserRepository(),
) {
    private val store = InMemoryIdempotencyStore<CreateUserResponse>()
    private val executor = IdempotencyExecutor(store)

    val log get() = executor.log

    fun createUser(idempotencyKey: IdempotencyKey, request: CreateUserRequest): IdempotencyResult<CreateUserResponse> {
        return executor.execute(idempotencyKey) {
            repository.save(request)
        }
    }

    val userCount get() = repository.count()
}
