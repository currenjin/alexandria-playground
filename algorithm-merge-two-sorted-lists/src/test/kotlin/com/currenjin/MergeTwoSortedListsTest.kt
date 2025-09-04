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
}
