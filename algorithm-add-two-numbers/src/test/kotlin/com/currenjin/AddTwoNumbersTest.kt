package com.currenjin

import org.junit.jupiter.api.Test
import java.util.LinkedList
import kotlin.test.assertEquals

class AddTwoNumbersTest {
    @Test
    fun add_one_number() {
        val node1 = LinkedList<Int>()
        val node2 = LinkedList<Int>()
        node1.add(4)
        node2.add(4)

        val actual = AddTwoNumbers.add(node1, node2)

        assertEquals(listOf(8), actual)
    }

    @Test
    fun addOneNumbers_returnsTwoNumbers() {
        val node1 = LinkedList<Int>()
        val node2 = LinkedList<Int>()
        node1.add(4)
        node2.add(8)

        val actual = AddTwoNumbers.add(node1, node2)

        assertEquals(listOf(2, 1), actual)
    }

    @Test
    fun addsTwoNumbers_sameLength_withIntermediateCarry() {
        val node1 = LinkedList<Int>()
        val node2 = LinkedList<Int>()
        node1.add(2)
        node1.add(4)
        node1.add(3)

        node2.add(5)
        node2.add(6)
        node2.add(4)

        val actual = AddTwoNumbers.add(node1, node2)

        assertEquals(listOf(7, 0, 8), actual)
    }
}
