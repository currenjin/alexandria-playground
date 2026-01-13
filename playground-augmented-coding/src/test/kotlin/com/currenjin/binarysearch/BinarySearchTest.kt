package com.currenjin.binarysearch

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BinarySearchTest {

    @Test
    fun `search in empty list returns NotFound`() {
        val result = BinarySearch.search(emptyList<Int>(), 1)

        assertThat(result).isEqualTo(BinarySearchResult.NotFound<Int>(0))
    }
}
