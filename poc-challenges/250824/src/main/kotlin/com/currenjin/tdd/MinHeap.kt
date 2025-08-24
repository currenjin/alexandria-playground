package com.currenjin.tdd

class MinHeap {
    private val data = arrayListOf<Int>()

    fun isEmpty(): Boolean {
        return data.isEmpty()
    }

    fun add(i: Int) {
        data.add(i)
    }
}