package com.currenjin

class BinarySearch {
    companion object {
        fun search(array: Array<Int>, target: Int): Int {
            return when {
                array.isEmpty() && array[0] == target -> 0
                array.size > 1 && array[1] == target -> 1
                else -> -1
            }
        }
    }
}
