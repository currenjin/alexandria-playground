package com.currenjin

class SegmentTree(
    private val data: Array<Int>
) {
    fun query(left: Int, right: Int): Int {
        var sum = 0

        for (i in left..right) {
            sum += data[i]
        }

        return sum
    }
}
