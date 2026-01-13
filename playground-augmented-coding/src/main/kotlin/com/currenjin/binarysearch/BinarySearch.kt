package com.currenjin.binarysearch

object BinarySearch {
    fun <T : Comparable<T>> search(list: List<T>, target: T): BinarySearchResult<T> {
        for (i in list.indices) {
            if (list[i] == target) {
                return BinarySearchResult.Found(i)
            }
        }
        return BinarySearchResult.NotFound(0)
    }
}
