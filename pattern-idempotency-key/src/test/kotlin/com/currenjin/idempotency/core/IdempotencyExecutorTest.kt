package com.currenjin.idempotency.core

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IdempotencyExecutorTest {

    private lateinit var store: InMemoryIdempotencyStore<String>
    private lateinit var executor: IdempotencyExecutor<String>

    @BeforeEach
    fun setUp() {
        store = InMemoryIdempotencyStore()
        executor = IdempotencyExecutor(store)
    }

    @Test
    fun `새로운 키로 실행하면 작업이 수행된다`() {
        val key = IdempotencyKey("new-key")

        val result = executor.execute(key) { "결과" }

        assertThat(result).isInstanceOf(IdempotencyResult.Executed::class.java)
        assertThat((result as IdempotencyResult.Executed).response).isEqualTo("결과")
    }

    @Test
    fun `동일한 키로 재실행하면 캐시된 응답이 반환된다`() {
        val key = IdempotencyKey("same-key")
        executor.execute(key) { "첫 번째 결과" }

        val result = executor.execute(key) { "두 번째 결과" }

        assertThat(result).isInstanceOf(IdempotencyResult.Replayed::class.java)
        assertThat((result as IdempotencyResult.Replayed).response).isEqualTo("첫 번째 결과")
    }

    @Test
    fun `동일한 키로 여러 번 재실행해도 항상 첫 번째 결과가 반환된다`() {
        val key = IdempotencyKey("multi-key")
        executor.execute(key) { "원본 결과" }

        val second = executor.execute(key) { "두 번째" }
        val third = executor.execute(key) { "세 번째" }

        assertThat((second as IdempotencyResult.Replayed).response).isEqualTo("원본 결과")
        assertThat((third as IdempotencyResult.Replayed).response).isEqualTo("원본 결과")
    }

    @Test
    fun `작업 실패 후 동일 키로 재실행하면 다시 수행된다`() {
        val key = IdempotencyKey("retry-key")
        var shouldFail = true

        assertThatThrownBy {
            executor.execute(key) {
                if (shouldFail) throw RuntimeException("실패")
                "결과"
            }
        }.isInstanceOf(RuntimeException::class.java)

        shouldFail = false
        val result = executor.execute(key) { "재시도 결과" }

        assertThat(result).isInstanceOf(IdempotencyResult.Executed::class.java)
        assertThat((result as IdempotencyResult.Executed).response).isEqualTo("재시도 결과")
    }

    @Test
    fun `서로 다른 키는 각각 독립적으로 실행된다`() {
        val key1 = IdempotencyKey("key-1")
        val key2 = IdempotencyKey("key-2")

        val result1 = executor.execute(key1) { "결과 1" }
        val result2 = executor.execute(key2) { "결과 2" }

        assertThat(result1).isInstanceOf(IdempotencyResult.Executed::class.java)
        assertThat(result2).isInstanceOf(IdempotencyResult.Executed::class.java)
        assertThat((result1 as IdempotencyResult.Executed).response).isEqualTo("결과 1")
        assertThat((result2 as IdempotencyResult.Executed).response).isEqualTo("결과 2")
    }

    @Test
    fun `실행 로그가 기록된다`() {
        val key = IdempotencyKey("log-key")
        executor.execute(key) { "결과" }
        executor.execute(key) { "결과" }

        assertThat(executor.log).hasSize(2)
        assertThat(executor.log[0]).contains("작업 실행 완료")
        assertThat(executor.log[1]).contains("중복 요청 감지")
    }
}
