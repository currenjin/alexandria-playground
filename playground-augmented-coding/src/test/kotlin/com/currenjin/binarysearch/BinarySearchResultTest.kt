package com.currenjin.binarysearch

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BinarySearchResultTest {

    @Test
    fun `BinarySearchResult has Found and NotFound subclasses`() {
        val found: BinarySearchResult<Int> = BinarySearchResult.Found(0)
        val notFound: BinarySearchResult<Int> = BinarySearchResult.NotFound(0)

        assertThat(found).isInstanceOf(BinarySearchResult::class.java)
        assertThat(notFound).isInstanceOf(BinarySearchResult::class.java)
    }

    @Test
    fun `Found contains index value`() {
        val found = BinarySearchResult.Found<Int>(5)

        assertThat(found.index).isEqualTo(5)
    }

    @Test
    fun `NotFound contains insertionPoint value`() {
        val notFound = BinarySearchResult.NotFound<Int>(3)

        assertThat(notFound.insertionPoint).isEqualTo(3)
    }
}
