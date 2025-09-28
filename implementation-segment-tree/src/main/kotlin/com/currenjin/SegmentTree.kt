package com.currenjin

class SegmentTree(
    private val data: Array<Int>
) {
    private val n = data.size
    private val tree = IntArray(4 * n)

    init {
        buildTree(1, 0, n - 1)
    }

    private fun buildTree(node: Int, start: Int, end: Int) {
        if (start == end) {
            tree[node] = data[start]
        }
    }

    fun query(left: Int, right: Int): Int {
        var sum = 0

        for (i in left..right) {
            sum += data[i]
        }

        return sum
    }

    fun update(index: Int, value: Int) {
        data[index] = value
    }
}
