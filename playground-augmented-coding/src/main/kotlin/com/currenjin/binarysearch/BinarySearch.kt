package com.currenjin.binarysearch

object BinarySearch {
    fun <T : Comparable<T>> search(list: List<T>, target: T): BinarySearchResult<T> {
        var low = 0
        var high = list.size - 1

        while (low <= high) {
            val mid = (low + high) / 2
            val comparison = list[mid].compareTo(target)

            if (comparison == 0) {
                return BinarySearchResult.Found(mid)
            }
            if (comparison < 0) {
                low = mid + 1
            } else {
                high = mid - 1
            }
        }

        return BinarySearchResult.NotFound(low)
    }
}
