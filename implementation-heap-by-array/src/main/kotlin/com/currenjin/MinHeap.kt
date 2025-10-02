package com.currenjin

class MinHeap {
    private var top: Int? = null
    private var count: Int = 0

    fun isEmpty(): Boolean = count == 0

    fun add(value: Int) {
        count += 1
        top = if (top == null || value < top!!) value else top
    }

    fun peek(): Int = top!!

    fun poll(): Int {
        val result = top!!
        count -= 1
        if (count == 0) top = null
        return result
    }
}
