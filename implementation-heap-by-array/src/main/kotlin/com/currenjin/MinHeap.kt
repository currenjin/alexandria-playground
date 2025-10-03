package com.currenjin

class MinHeap {
    private val data = mutableListOf<Int>()

    fun isEmpty(): Boolean = data.isEmpty()

    fun add(value: Int) {
        data += value
    }

    fun peek(): Int = data.minOrNull()!!

    fun poll(): Int {
        val min = peek()
        data.remove(min)
        return min
    }
}
