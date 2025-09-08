package com.currenjin.tdd

class MyHashMap<K: Any, V> {
    private data class Entry<K, V>(val key: K, var value: V)

    private var buckets: Array<MutableList<Entry<K, V>>> = Array(4) { mutableListOf() }
    private var _size: Int = 0
    private val loadFactor = 0.75f

    val size: Int get() = _size
    fun isEmpty(): Boolean = _size == 0

    private fun index(key: K, capacity: Int = buckets.size): Int = (key.hashCode() and 0x7ffffff) % capacity
    private fun bucket(key: K) = buckets[index(key)]

    fun containsKey(key: K): Boolean = bucket(key).any { it.key == key }

    fun get(key: K): V? = bucket(key).firstOrNull { it.key == key }?.value

    fun put(key: K, value: V) {
        if ((_size + 1) > (buckets.size * loadFactor)) {
            resize(buckets.size * 2)
        }

        val bucket = bucket(key)
        val matched = bucket.firstOrNull { it.key == key }
        if (matched != null) {
            matched.value = value
        } else {
            bucket.add(Entry(key, value))
            _size++
        }
    }

    fun remove(key: K) {
        val bucket = bucket(key)
        val index = bucket.indexOfFirst { it.key == key }
        if (index >= 0) {
            bucket.removeAt(index)
            _size--
        }
    }

    private fun resize(newCapacity: Int) {
        val old = buckets
        buckets = Array(newCapacity) { mutableListOf<Entry<K, V>>() }

        for (list in old) {
            for (element in list) {
                buckets[index(element.key, newCapacity)].add(element)
            }
        }
    }
}
