package com.currenjin.idempotency.api

data class CreateUserResponse(
    val userId: String,
    val email: String,
    val name: String,
)
