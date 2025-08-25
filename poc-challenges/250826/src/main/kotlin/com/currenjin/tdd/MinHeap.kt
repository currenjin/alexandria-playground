package com.currenjin.tdd

class MinHeap {
    private val data = arrayListOf<Int>()

    fun isEmpty() = data.isEmpty()

    fun add(i: Int) {
        data.add(i)
        siftUp(data.lastIndex)
    }

    fun peek(): Int {
        validateEmptyHeap()

        return data[0]
    }

    fun poll(): Int {
        validateEmptyHeap()

        return data.removeAt(0)
    }

    private fun siftUp(lastIndex: Int) {
        var index = lastIndex

        while (index > 0) {
            val parentIndex = (index - 1) / 2
            if (data[index] < data[parentIndex]) {
                data[index] = data[parentIndex].also { data[parentIndex] = data[index] }
                index = parentIndex
            } else {
                break
            }
        }
    }

    private fun validateEmptyHeap() {
        if (isEmpty()) throw NoSuchElementException("Heap is empty")
    }
}
