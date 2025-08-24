package com.currenjin.tdd

class MinHeap {
    private val data = arrayListOf<Int>()

    fun isEmpty(): Boolean {
        return data.isEmpty()
    }

    fun add(i: Int) {
        data.add(i)
    }

    fun peek(): Int {
        if (data.isEmpty()) {
            throw NoSuchElementException("Heap is empty")
        }

        return data[0]
    }
}