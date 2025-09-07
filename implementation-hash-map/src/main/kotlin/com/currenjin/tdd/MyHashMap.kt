package com.currenjin.tdd

class MyHashMap<K: Any, V> {
    var size: Int = 0

    private var lastKey: K? = null

    fun isEmpty(): Boolean {
        return size == 0
    }

    fun containsKey(key: K): Boolean {
        return lastKey != null && lastKey == key
    }

    fun get(key: K): V? {
        return null
    }

    fun put(key: K, value: V) {
        if (size == 0) size = 1
        lastKey = key
    }
}
