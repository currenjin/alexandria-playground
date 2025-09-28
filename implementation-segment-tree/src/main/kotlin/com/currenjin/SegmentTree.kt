package com.currenjin

class SegmentTree(
    private val data: Array<Int>
) {
    fun query(left: Int, right: Int): Int {
        return data[left]
    }
}
