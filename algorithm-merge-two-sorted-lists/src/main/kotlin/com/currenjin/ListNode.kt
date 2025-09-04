package com.currenjin

data class ListNode(
    var `val`: Int,
    var next: ListNode? = null,
)

fun ListNode?.toList(): List<Int> {
    val result = mutableListOf<Int>()
    var curr = this
    while (curr != null) {
        result.add(curr.`val`)
        curr = curr.next
    }
    return result
}

fun List<Int>.toListNode(): ListNode? {
    if (isEmpty()) return null
    val head = ListNode(this[0])
    var curr = head
    for (i in 1 until size) {
        curr.next = ListNode(this[i])
        curr = curr.next!!
    }
    return head
}
