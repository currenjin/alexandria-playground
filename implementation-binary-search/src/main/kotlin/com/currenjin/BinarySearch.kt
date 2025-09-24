package com.currenjin

class BinarySearch {
    companion object {
        fun search(array: Array<Int>, target: Int): Int {
            var left = 0
            var right = array.size - 1

            while (left <= right) {
                val mid = (left + right) / 2
                when {
                    array[mid] == target -> return mid
                    target < array[mid] -> right = mid - 1
                    else -> left = mid + 1
                }
            }

            return -1
        }
    }
}
