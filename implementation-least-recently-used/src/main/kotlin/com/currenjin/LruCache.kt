package com.currenjin

class LruCache<K, V> : Iterable<K> {
    companion object {
        private const val DEFAULT_CAPACITY = 1
    }

    constructor(capacity: Int = DEFAULT_CAPACITY) {
        validateCapacity(capacity)

        this.capacity = capacity
    }

    var capacity: Int = DEFAULT_CAPACITY
        set(value) {
            validateCapacity(value)
            field = value
            while (store.size > field) {
                removeOldest()
            }
        }

    private val store = LinkedHashMap<K, V>(16, 0.75f, true)

    fun get(key: K): V? = store[key]

    fun put(
        key: K,
        value: V,
    ) {
        store[key] = value
        if (store.size > capacity) removeOldest()
    }

    fun size(): Int = store.size

    fun clear() = store.clear()

    fun remove(key: K) = store.remove(key)

    fun contains(key: K): Boolean = store.containsKey(key)

    override fun iterator(): Iterator<K> = store.keys.iterator()

    private fun validateCapacity(value: Int) {
        require(value > 0) { "Capacity must be greater than 0" }
    }

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
