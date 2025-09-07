package com.currenjin.tdd

class MyHashMap<K: Any, V> {
    private data class Entry<K, V>(val key: K, var value: V)

    private val entries = mutableListOf<Entry<K, V>>()
    val size: Int get() = entries.size
    fun isEmpty(): Boolean = entries.isEmpty()

    fun containsKey(key: K): Boolean {
        return entries.any { it.key == key }
    }

    fun get(key: K): V? {
        return entries.firstOrNull { it.key == key }?.value
    }

    fun put(key: K, value: V) {
        val found = entries.firstOrNull { it.key == key }
        if (found != null) {
            found.value = value
        } else {
            entries.add(Entry(key, value))
        }
    }
}
