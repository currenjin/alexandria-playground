package com.currenjin.binarysearch

object BinarySearch {
    fun <T : Comparable<T>> search(list: List<T>, target: T): BinarySearchResult<T> {
        if (list.isNotEmpty() && list[0] == target) {
            return BinarySearchResult.Found(0)
        }
        return BinarySearchResult.NotFound(0)
    }
}
