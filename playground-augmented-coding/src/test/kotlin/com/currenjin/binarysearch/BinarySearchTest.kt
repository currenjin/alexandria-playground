package com.currenjin.binarysearch

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BinarySearchTest {

    @Test
    fun `search in empty list returns NotFound`() {
        val result = BinarySearch.search(emptyList<Int>(), 1)

        assertThat(result).isEqualTo(BinarySearchResult.NotFound<Int>(0))
    }

    @Test
    fun `search for existing element in single element list returns Found`() {
        val result = BinarySearch.search(listOf(5), 5)

        assertThat(result).isEqualTo(BinarySearchResult.Found<Int>(0))
    }
}
