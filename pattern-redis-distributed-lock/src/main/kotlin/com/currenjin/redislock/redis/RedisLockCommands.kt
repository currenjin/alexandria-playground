package com.currenjin.redislock.redis

import kotlin.time.Duration

interface RedisLockCommands {
    fun setIfAbsent(key: String, value: String, ttl: Duration): Boolean
    fun compareAndDelete(key: String, expectedValue: String): Boolean
    fun compareAndExpire(key: String, expectedValue: String, ttl: Duration): Boolean
}
