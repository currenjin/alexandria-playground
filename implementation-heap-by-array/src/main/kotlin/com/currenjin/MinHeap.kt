package com.currenjin

class MinHeap {
    private var top: Int? = null
    private var count: Int = 0

    fun isEmpty(): Boolean = count == 0

    fun add(value: Int) {
        count += 1
        top = value
    }

    fun peek(): Int = top!!
}
