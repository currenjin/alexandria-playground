package com.currenjin.binarysearch

sealed class BinarySearchResult<T> {
    data class Found<T>(val index: Int) : BinarySearchResult<T>()
    data class NotFound<T>(val insertionPoint: Int) : BinarySearchResult<T>()
}
