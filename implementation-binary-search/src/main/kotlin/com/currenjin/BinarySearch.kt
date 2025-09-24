package com.currenjin

class BinarySearch {
    companion object {
        fun search(array: Array<Int>, target: Int): Int {
            if (array.isEmpty()) return -1

            val mid = array.size / 2
            return when {
                array[mid] == target -> mid
                target < array[mid] && array[0] == target -> 0
                else -> -1
            }
        }
    }
}
