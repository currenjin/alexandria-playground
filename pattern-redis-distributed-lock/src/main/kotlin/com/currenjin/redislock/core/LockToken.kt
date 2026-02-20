package com.currenjin.redislock.core

import java.util.UUID

@JvmInline
value class LockToken(val value: String) {
    companion object {
        fun random(): LockToken = LockToken(UUID.randomUUID().toString())
    }
}
