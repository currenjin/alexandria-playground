package com.currenjin.redislock.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

class RedisDistributedLockTest {
    private val redis = InMemoryRedisLockCommands()
    private val lock = RedisDistributedLock(redis)

    @Test
    fun `첫 번째 요청은 락을 획득한다`() {
        val result = lock.acquire(LockKey("lock:order:1"), 3.seconds)

        assertThat(result).isInstanceOf(LockAcquireResult.Acquired::class.java)
    }

    @Test
    fun `락이 이미 점유중이면 획득에 실패한다`() {
        lock.acquire(LockKey("lock:order:1"), 3.seconds)

        val second = lock.acquire(LockKey("lock:order:1"), 3.seconds)

        assertThat(second).isEqualTo(LockAcquireResult.Rejected)
    }

    @Test
    fun `락 소유자는 해제할 수 있다`() {
        val key = LockKey("lock:order:1")
        val acquired = lock.acquire(key, 3.seconds)
        val token = (acquired as LockAcquireResult.Acquired).token

        val released = lock.release(key, token)
        val reacquired = lock.acquire(key, 3.seconds)

        assertThat(released).isTrue()
        assertThat(reacquired).isInstanceOf(LockAcquireResult.Acquired::class.java)
    }

    @Test
    fun `락 소유자가 아니면 해제할 수 없다`() {
        val key = LockKey("lock:order:1")
        lock.acquire(key, 3.seconds)

        val released = lock.release(key, LockToken.random())

        assertThat(released).isFalse()
    }

    @Test
    fun `TTL이 만료되면 다른 요청이 락을 획득할 수 있다`() {
        val key = LockKey("lock:order:1")
        lock.acquire(key, 3.seconds)
        redis.advanceTimeBy(4.seconds)

        val reacquired = lock.acquire(key, 3.seconds)

        assertThat(reacquired).isInstanceOf(LockAcquireResult.Acquired::class.java)
    }

    @Test
    fun `락 소유자는 TTL을 갱신할 수 있다`() {
        val key = LockKey("lock:order:1")
        val acquired = lock.acquire(key, 3.seconds)
        val token = (acquired as LockAcquireResult.Acquired).token

        redis.advanceTimeBy(2.seconds)
        val renewed = lock.renew(key, token, 3.seconds)

        redis.advanceTimeBy(2.seconds)
        val stillLocked = lock.acquire(key, 3.seconds)

        redis.advanceTimeBy(2.seconds)
        val finallyAcquired = lock.acquire(key, 3.seconds)

        assertThat(renewed).isTrue()
        assertThat(stillLocked).isEqualTo(LockAcquireResult.Rejected)
        assertThat(finallyAcquired).isInstanceOf(LockAcquireResult.Acquired::class.java)
    }
}
