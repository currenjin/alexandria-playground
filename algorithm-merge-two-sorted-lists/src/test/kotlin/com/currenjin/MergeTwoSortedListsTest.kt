package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MergeTwoSortedListsTest {
    @Test
    fun sortedArray_still_returnsIt() {
        val actual = MergeTwoSortedLists.mergeTwoLists(listOf(1, 2, 3).toListNode(), listOf<Int>().toListNode())

        assertEquals(listOf(1, 2, 3).toListNode(), actual)
    }

    @Test
    fun merge_two_single_nodes() {
        val actual = MergeTwoSortedLists.mergeTwoLists(listOf(2).toListNode(), listOf(1).toListNode())

        assertEquals(listOf(1, 2).toListNode(), actual)
    }

    @Test
    fun merge_two_lists_same_length() {
        val actual = MergeTwoSortedLists.mergeTwoLists(listOf(1, 3, 5).toListNode(), listOf(2, 4, 6).toListNode())

        assertEquals(listOf(1, 2, 3, 4, 5, 6).toListNode(), actual)
    }

    @Test
    fun merge_two_lists_different_length() {
        val actual = MergeTwoSortedLists.mergeTwoLists(listOf(1, 2, 4).toListNode(), listOf(1, 3, 4, 5, 6).toListNode())

        assertEquals(listOf(1, 1, 2, 3, 4, 4, 5, 6).toListNode(), actual)
    }
}
