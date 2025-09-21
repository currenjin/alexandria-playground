package com.currenjin

class LruCache<K, V> {
    companion object {
        private const val INITIAL_CAPACITY = 1
    }

    constructor(capacity: Int = INITIAL_CAPACITY) {
        require(capacity > 0) { "Capacity must be greater than 0" }

        this.capacity = capacity
    }

    var capacity: Int = INITIAL_CAPACITY
        set(value) {
            field = value
            removeOldest()
        }

    private val store = LinkedHashMap<K, V>(16, 0.75f, true)

    fun get(key: K): V? = store[key]

    fun put(
        key: K,
        value: V,
    ) {
        store[key] = value
        removeOldest()
    }

    fun size(): Int = store.size

    fun clear() {
        store.clear()
    }

    fun remove(key: K) {
        store.remove(key)
    }

    fun contains(key: K): Boolean = store.containsKey(key)

    fun iterator(): Iterator<K> = store.keys.iterator()

    private fun removeOldest() {
        if (store.size > capacity) {
            val oldestKey =
                store.entries
                    .iterator()
                    .next()
                    .key

            store.remove(oldestKey)
        }
    }
}
