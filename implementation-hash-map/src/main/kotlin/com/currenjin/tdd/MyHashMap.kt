package com.currenjin.tdd

class MyHashMap<K: Any, V> {
    private data class Entry<K, V>(val key: K, var value: V)

    private var buckets: Array<MutableList<Entry<K, V>>> = Array(4) { mutableListOf() }

    val size: Int get() = buckets.sumOf { it.size }
    fun isEmpty(): Boolean = size == 0

    private fun index(key: K): Int = (key.hashCode() and 0x7ffffff) % buckets.size
    private fun bucket(key: K) = buckets[index(key)]

    fun containsKey(key: K): Boolean = bucket(key).any { it.key == key }

    fun get(key: K): V? = bucket(key).firstOrNull { it.key == key }?.value

    fun put(key: K, value: V) {
        val bucket = bucket(key)
        val matchedBucket = bucket.firstOrNull { it.key == key }
        if (matchedBucket != null) matchedBucket.value = value else bucket.add(Entry(key, value))
    }

    fun remove(key: K) {
        val bucket = bucket(key)
        val index = bucket.indexOfFirst { it.key == key }
        if (index >= 0) bucket.removeAt(index)
    }
}
