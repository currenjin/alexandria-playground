package com.currenjin.redislock.core

sealed interface LockAcquireResult {
    data class Acquired(val token: LockToken) : LockAcquireResult
    data object Rejected : LockAcquireResult
}
