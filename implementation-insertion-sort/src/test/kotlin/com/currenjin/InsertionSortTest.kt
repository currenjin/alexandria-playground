package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.emptyArray
import kotlin.test.assertContentEquals

class InsertionSortTest {
    @Test
    fun empty_array_returns_empty_array() {
        val actual = InsertionSort.sort(emptyArray())

        assertContentEquals(emptyArray(), actual)
    }
}