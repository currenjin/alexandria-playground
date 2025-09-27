package com.currenjin

private const val NOT_FOUND = -1

class BinarySearch {
    companion object {
        fun <T : Comparable<T>>search(array: Array<T>, target: T): Int =
            searchInternal(array, target, moveLeftOnMatch = true)

        fun <T : Comparable<T>>searchLast(array: Array<T>, target: T): Int =
            searchInternal(array, target, moveLeftOnMatch = false)

        private fun <T : Comparable<T>>searchInternal(
            array: Array<T>,
            target: T,
            moveLeftOnMatch: Boolean
        ): Int {
            var left = 0
            var right = array.size - 1
            var result = NOT_FOUND

            while (left <= right) {
                val mid = left + (right - left) / 2
                when {
                    array[mid] == target -> {
                        result = mid
                        if (moveLeftOnMatch) {
                            right = mid - 1
                        } else {
                            left = mid + 1
                        }
                    }
                    target < array[mid] -> right = mid - 1
                    else -> left = mid + 1
                }
            }
            return result
        }
    }
}
