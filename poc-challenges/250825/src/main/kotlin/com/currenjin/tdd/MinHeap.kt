package com.currenjin.tdd

class MinHeap {
    private val data = arrayListOf<Int>()

    fun isEmpty() = data.isEmpty()

    fun add(i: Int) {
        data.add(i)
    }

    fun peek(): Int {
        if (isEmpty()) throw NoSuchElementException("Heap is empty")

        return data.last()
    }
}
