package com.currenjin

private const val NOT_FOUND = -1

class BinarySearch {
    companion object {
        fun search(array: Array<Int>, target: Int): Int {
            var left = 0
            var right = array.size - 1
            var result = NOT_FOUND

            while (left <= right) {
                val mid = (left + right) / 2
                when {
                    array[mid] == target -> {
                        result = mid
                        right = mid - 1
                    }
                    target < array[mid] -> right = mid - 1
                    else -> left = mid + 1
                }
            }

            return result
        }
    }
}
