package com.currenjin

class LruCache(
    private val capacity: Int = 1,
) {
    private val store = LinkedHashMap<Int, String>(16, 0.75f, true)

    init {
        require(capacity > 0) { "Capacity must be greater than 0" }
    }

    fun get(key: Int): String? = store[key]

    fun put(
        key: Int,
        value: String,
    ) {
        store[key] = value
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
