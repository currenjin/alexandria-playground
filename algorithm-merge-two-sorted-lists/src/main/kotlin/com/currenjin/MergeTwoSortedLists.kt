package com.currenjin

class MergeTwoSortedLists {
    companion object {
        fun mergeTwoLists(
            list1: ListNode?,
            list2: ListNode?,
        ): ListNode? {
            var p1 = list1
            var p2 = list2
            val dummy = ListNode(0)
            var tail = dummy

            while (p1 != null && p2 != null) {
                if (p1.`val` <= p2.`val`) {
                    tail.next = p1
                    p1 = p1.next
                } else {
                    tail.next = p2
                    p2 = p2.next
                }
                tail = tail.next!!
            }
            tail.next = p1 ?: p2
            return dummy.next
        }
    }
}
