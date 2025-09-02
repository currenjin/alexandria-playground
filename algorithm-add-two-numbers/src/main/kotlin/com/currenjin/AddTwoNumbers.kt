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

            val numberOfNode1 = node1.reversed().joinToString { it.toString() }.toInt()
            val numberOfNode2 = node2.reversed().joinToString { it.toString() }.toInt()

            val result = numberOfNode1 + numberOfNode2
            val splitResult =
                result
                    .toString()
                    .split("")
                    .mapNotNull {
                        try {
                            it.toInt()
                        } catch (e: NumberFormatException) {
                            return@mapNotNull null
                        }
                    }.reversed()

            return LinkedList(splitResult)
        }
    }
}
