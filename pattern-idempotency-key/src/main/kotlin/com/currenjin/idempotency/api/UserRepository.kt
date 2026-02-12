package com.currenjin.idempotency.api

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class UserRepository {
    private val users = ConcurrentHashMap<String, CreateUserResponse>()

    fun save(request: CreateUserRequest): CreateUserResponse {
        val response = CreateUserResponse(
            userId = UUID.randomUUID().toString(),
            email = request.email,
            name = request.name,
        )
        users[response.userId] = response
        return response
    }

    fun count(): Int = users.size

    fun findAll(): List<CreateUserResponse> = users.values.toList()
}
