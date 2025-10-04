package com.currenjin

class MinHeap {
    private val data = mutableListOf<Int>()

    fun isEmpty(): Boolean = data.isEmpty()

    fun add(value: Int) {
        data += value
    }

    fun peek(): Int = data.minOrNull() ?: throw NoSuchElementException("empty heap")

    fun poll(): Int {
        if (data.isEmpty()) throw NoSuchElementException("empty heap")

        val min = peek()
        data.remove(min)
        return min
    }
}
