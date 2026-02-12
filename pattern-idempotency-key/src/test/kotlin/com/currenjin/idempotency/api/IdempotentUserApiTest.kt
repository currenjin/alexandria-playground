package com.currenjin.idempotency.api

import com.currenjin.idempotency.core.IdempotencyKey
import com.currenjin.idempotency.core.IdempotencyResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IdempotentUserApiTest {

    private lateinit var api: IdempotentUserApi

    @BeforeEach
    fun setUp() {
        api = IdempotentUserApi()
    }

    @Test
    fun `사용자 생성이 정상적으로 처리된다`() {
        val key = IdempotencyKey("user-001")
        val request = CreateUserRequest(email = "test@example.com", name = "홍길동")

        val result = api.createUser(key, request)

        assertThat(result).isInstanceOf(IdempotencyResult.Executed::class.java)
        val response = (result as IdempotencyResult.Executed).response
        assertThat(response.email).isEqualTo("test@example.com")
        assertThat(response.name).isEqualTo("홍길동")
        assertThat(api.userCount).isEqualTo(1)
    }

    @Test
    fun `동일한 멱등성 키로 중복 생성을 방지한다`() {
        val key = IdempotencyKey("user-002")
        val request = CreateUserRequest(email = "dup@example.com", name = "김철수")

        api.createUser(key, request)
        val second = api.createUser(key, request)

        assertThat(second).isInstanceOf(IdempotencyResult.Replayed::class.java)
        assertThat(api.userCount).isEqualTo(1)
    }

    @Test
    fun `중복 요청 시 동일한 사용자 ID가 반환된다`() {
        val key = IdempotencyKey("user-003")
        val request = CreateUserRequest(email = "same@example.com", name = "이영희")

        val first = api.createUser(key, request) as IdempotencyResult.Executed
        val second = api.createUser(key, request) as IdempotencyResult.Replayed

        assertThat(first.response.userId).isEqualTo(second.response.userId)
    }

    @Test
    fun `서로 다른 멱등성 키로 동일한 요청을 보내면 각각 생성된다`() {
        val request = CreateUserRequest(email = "multi@example.com", name = "박지민")

        api.createUser(IdempotencyKey("user-004a"), request)
        api.createUser(IdempotencyKey("user-004b"), request)

        assertThat(api.userCount).isEqualTo(2)
    }
}
