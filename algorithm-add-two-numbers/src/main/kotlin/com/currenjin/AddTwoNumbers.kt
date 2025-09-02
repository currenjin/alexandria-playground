package com.currenjin

import java.util.LinkedList

class AddTwoNumbers {
    companion object {
        fun add(
            node1: LinkedList<Int>,
            node2: LinkedList<Int>,
        ): LinkedList<Int> {
            val list = LinkedList<Int>()
            list.add(node1.element() + node2.element())
            return list
        }
    }
}
