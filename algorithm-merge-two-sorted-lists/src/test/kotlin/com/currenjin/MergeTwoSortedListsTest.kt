package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MergeTwoSortedListsTest {
    @Test
    fun sortedArray_still_returnsIt() {
        val actual = MergeTwoSortedLists.mergeTwoLists(listOf(1, 2, 3), listOf())

        assertEquals(listOf(1, 2, 3), actual)
    }
}
