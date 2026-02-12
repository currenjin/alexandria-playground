package com.currenjin.idempotency.api

data class CreateUserRequest(
    val email: String,
    val name: String,
)
