package com.currenjin.tdd

class MyHashMap<K: Any, V> {
    fun isEmpty(): Boolean {
        return true
    }

    fun containsKey(key: K): Boolean {
        return false
    }

    fun get(string: K): V? {
        return null
    }

    val size: Int = 0
}
