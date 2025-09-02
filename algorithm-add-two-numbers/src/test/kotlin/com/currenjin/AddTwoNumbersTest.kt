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
}
