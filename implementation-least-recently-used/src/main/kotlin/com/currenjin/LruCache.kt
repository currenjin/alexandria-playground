package com.currenjin

class LruCache<K, V>(
    private val capacity: Int = 1,
) {
    private val store = LinkedHashMap<K, V>(16, 0.75f, true)

    init {
        require(capacity > 0) { "Capacity must be greater than 0" }
    }

    fun get(key: K): V? = store[key]

    fun put(
        key: K,
        value: V,
    ) {
        store[key] = value
        evictIfNeeded()
    }

    private fun evictIfNeeded() {
        if (store.size > capacity) {
            val eldestKey =
                store.entries
                    .iterator()
                    .next()
                    .key

            store.remove(eldestKey)
        }
    }
}
