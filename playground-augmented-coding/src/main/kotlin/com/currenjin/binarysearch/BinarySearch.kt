package com.currenjin.binarysearch

object BinarySearch {
    fun <T : Comparable<T>> search(list: List<T>, target: T): BinarySearchResult<T> {
        return BinarySearchResult.NotFound(0)
    }
}
