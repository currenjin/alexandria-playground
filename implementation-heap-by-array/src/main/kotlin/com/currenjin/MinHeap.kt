package com.currenjin

class MinHeap<T : Comparable<T>>(
    private val comparator: Comparator<T> = Comparator.naturalOrder(),
) {
    private val data = mutableListOf<T>()

    fun size(): Int = data.size

    fun isEmpty(): Boolean = data.isEmpty()

    fun add(value: T) {
        data.add(value)
        siftUp(data.lastIndex)
    }

    fun peek(): T = data.firstOrNull() ?: throw NoSuchElementException("empty heap")

    fun poll(): T {
        if (data.isEmpty()) throw NoSuchElementException("empty heap")

        val result = data.first()
        val last = data.removeLast()
        if (data.isNotEmpty()) {
            data[0] = last
            siftDown(0)
        }
        return result
    }

    private fun siftUp(index: Int) {
        var i = index
        while (i > 0) {
            val parent = (i - 1) / 2
            if (comparator.compare(data[i], data[parent]) < 0) {
                swap(i, parent)
                i = parent
            } else {
                break
            }
        }
    }

    private fun siftDown(index: Int) {
        var i = index
        val last = data.lastIndex
        while (true) {
            val left = 2 * i + 1
            val right = 2 * i + 2
            var smallest = i
            if (left <= last && comparator.compare(data[left], data[smallest]) < 0) smallest = left
            if (right <= last && comparator.compare(data[right], data[smallest]) < 0) smallest = right
            if (smallest != i) {
                swap(i, smallest)
                i = smallest
            } else {
                break
            }
        }
    }

    private fun swap(
        i: Int,
        j: Int,
    ) {
        val tmp = data[i]
        data[i] = data[j]
        data[j] = tmp
    }
}
