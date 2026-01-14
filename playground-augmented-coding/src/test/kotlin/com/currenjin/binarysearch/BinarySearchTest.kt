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

    @Test
    fun `search for non-existing element in single element list returns NotFound`() {
        val result = BinarySearch.search(listOf(5), 3)

        assertThat(result).isEqualTo(BinarySearchResult.NotFound<Int>(0))
    }

    @Test
    fun `search for first element in multiple element list returns Found 0`() {
        val result = BinarySearch.search(listOf(1, 3, 5, 7, 9), 1)

        assertThat(result).isEqualTo(BinarySearchResult.Found<Int>(0))
    }

    @Test
    fun `search for last element in multiple element list returns Found lastIndex`() {
        val result = BinarySearch.search(listOf(1, 3, 5, 7, 9), 9)

        assertThat(result).isEqualTo(BinarySearchResult.Found<Int>(4))
    }

    @Test
    fun `search for middle element in multiple element list returns correct index`() {
        val result = BinarySearch.search(listOf(1, 3, 5, 7, 9), 5)

        assertThat(result).isEqualTo(BinarySearchResult.Found<Int>(2))
    }

    @Test
    fun `search for non-existing element in multiple element list returns NotFound`() {
        val result = BinarySearch.search(listOf(1, 3, 5, 7, 9), 4)

        assertThat(result).isEqualTo(BinarySearchResult.NotFound<Int>(2))
    }

    @Test
    fun `search for non-existing element returns insertion point`() {
        val result = BinarySearch.search(listOf(1, 3, 5, 7, 9), 6)

        assertThat(result).isEqualTo(BinarySearchResult.NotFound<Int>(3))
    }

    @Test
    fun `search for value smaller than all elements returns insertion point 0`() {
        val result = BinarySearch.search(listOf(1, 3, 5, 7, 9), 0)

        assertThat(result).isEqualTo(BinarySearchResult.NotFound<Int>(0))
    }

    @Test
    fun `search for value larger than all elements returns insertion point size`() {
        val result = BinarySearch.search(listOf(1, 3, 5, 7, 9), 10)

        assertThat(result).isEqualTo(BinarySearchResult.NotFound<Int>(5))
    }

    @Test
    fun `search works with comparable string`() {
        val result = BinarySearch.search(listOf("a", "b", "c", "d"), "c")

        assertThat(result).isEqualTo(BinarySearchResult.Found<String>(2))
    }
}
