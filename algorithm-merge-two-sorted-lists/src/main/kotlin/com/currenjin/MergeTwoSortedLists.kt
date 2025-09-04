package com.currenjin

class MergeTwoSortedLists {
    companion object {
        fun mergeTwoLists(
            list1: List<Int>,
            list2: List<Int>,
        ): List<Int> = (list1 + list2).sorted()
    }
}
