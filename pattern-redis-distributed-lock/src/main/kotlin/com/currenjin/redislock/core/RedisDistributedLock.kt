package com.currenjin.redislock.core

import com.currenjin.redislock.redis.RedisLockCommands
import kotlin.time.Duration

class RedisDistributedLock(
    private val commands: RedisLockCommands,
) {
    fun acquire(key: LockKey, ttl: Duration): LockAcquireResult {
        val token = LockToken.random()
        val acquired = commands.setIfAbsent(key.value, token.value, ttl)
        return if (acquired) {
            LockAcquireResult.Acquired(token)
        } else {
            LockAcquireResult.Rejected
        }
    }

    fun release(key: LockKey, token: LockToken): Boolean {
        return commands.compareAndDelete(key.value, token.value)
    }

    fun renew(key: LockKey, token: LockToken, ttl: Duration): Boolean {
        return commands.compareAndExpire(key.value, token.value, ttl)
    }
}
