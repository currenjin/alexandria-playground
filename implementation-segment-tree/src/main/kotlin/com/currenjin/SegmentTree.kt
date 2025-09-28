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
        } else {
            val mid = (start + end) / 2
            buildTree(node * 2, start, mid)
            buildTree(node * 2 + 1, mid + 1, end)
            tree[node] = tree[node * 2] + tree[node * 2 + 1]
        }
    }

    fun query(left: Int, right: Int): Int {
        return queryTree(1, 0, n - 1, left, right)
    }

    private fun queryTree(node: Int, start: Int, end: Int, left: Int, right: Int): Int {
        if (right < start || end < left) {
            return 0
        }
        if (left <= start && end <= right) {
            return tree[node]
        }
        val mid = (start + end) / 2
        val lSum = queryTree(node * 2, start, mid, left, right)
        val rSum = queryTree(node * 2 + 1, mid + 1, end, left, right)
        return lSum + rSum
    }

    fun update(index: Int, value: Int) {
        updateTree(1, 0, n - 1, index, value)
    }

    private fun updateTree(node: Int, start: Int, end: Int, idx: Int, value: Int) {
        if (start == end) {
            data[idx] = value
            tree[node] = value
        } else {
            val mid = (start + end) / 2
            if (idx <= mid) {
                updateTree(node * 2, start, mid, idx, value)
            } else {
                updateTree(node * 2 + 1, mid + 1, end, idx, value)
            }
            tree[node] = tree[node * 2] + tree[node * 2 + 1]
        }
    }
}
