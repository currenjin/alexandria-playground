package com.currenjin.tdd

class MinHeap {
    private val data = arrayListOf<Int>()

    fun isEmpty(): Boolean {
        return data.isEmpty()
    }

    fun add(i: Int) {
        data.add(i)
        siftUp(data.lastIndex)
    }

    fun peek(): Int {
        if (data.isEmpty()) {
            throw NoSuchElementException("Heap is empty")
        }

        return data[0]
    }

    fun poll(): Int {
        if (data.isEmpty()) {
            throw NoSuchElementException("Heap is empty")
        }

        if (data.size == 1) {
            return data.removeAt(0)
        }

        val root = data[0]
        val last = data.removeAt(data.lastIndex)
        data[0] = last
        shiftDown(0)

        return root
    }

    private fun shiftDown(i: Int) {
        var index = i
        val size = data.size

        while (true) {
            val left = index * 2 + 1
            val right = index + 1
            var smallest = index

            if (left < size && data[left] < data[smallest]) smallest = left
            if (right < size && data[right] < data[smallest]) smallest = right

            if (smallest == index) break
            data[index] = data[smallest].also { data[smallest] = data[index] }
            index = smallest
        }
    }

    private fun siftUp(lastIndex: Int) {
        var index = lastIndex

        while (index > 0) {
            val parentIndex = (index - 1) / 2
            if (data[index] < data[parentIndex]) {
                data[index] = data[parentIndex].also { data[parentIndex] = data[index] }
                index = parentIndex
            } else break
        }
    }
}
