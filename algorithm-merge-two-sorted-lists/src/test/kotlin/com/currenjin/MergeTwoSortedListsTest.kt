package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MergeTwoSortedListsTest {
    @Test
    fun sortedArray_still_returnsIt() {
        val actual = MergeTwoSortedLists.mergeTwoLists(listOf(1, 2, 3), listOf())

        assertEquals(listOf(1, 2, 3), actual)
    }

    @Test
    fun randomNumberList_returnsSortedList() {
        val actual = MergeTwoSortedLists.mergeTwoLists(listOf(2, 3, 1), listOf())

        assertEquals(listOf(1, 2, 3), actual)
    }

    @Test
    fun merge_two_single_nodes() {
        val actual = MergeTwoSortedLists.mergeTwoLists(listOf(2), listOf(1))

        assertEquals(listOf(1, 2), actual)
    }

    @Test
    fun merge_two_lists_same_length() {
        val actual = MergeTwoSortedLists.mergeTwoLists(listOf(1, 3, 5), listOf(2, 4, 6))

        assertEquals(listOf(1, 2, 3, 4, 5, 6), actual)
    }

    @Test
    fun merge_two_lists_different_length() {
        val actual = MergeTwoSortedLists.mergeTwoLists(listOf(1, 2, 4), listOf(1, 3, 4, 5, 6))

        assertEquals(listOf(1, 1, 2, 3, 4, 4, 5, 6), actual)
    }
}
