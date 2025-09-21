package com.currenjin

class LruCache {
    private val store = mutableMapOf<Int, String>()

    fun get(key: Int): String? {
        return store[key]
    }

    fun put(key: Int, value: String) {
        store[key] = value
    }
}