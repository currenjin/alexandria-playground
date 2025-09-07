package com.currenjin.tdd

class MyHashMap<K: Any, V> {
    var size: Int = 0

    fun isEmpty(): Boolean {
        return size == 0
    }

    fun containsKey(key: K): Boolean {
        return false
    }

    fun get(key: K): V? {
        return null
    }

    fun put(key: K, value: V) {
        if (size == 0) size = 1
    }
}
