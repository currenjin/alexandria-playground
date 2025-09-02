package com.currenjin

import org.junit.jupiter.api.Test
import java.util.LinkedList
import kotlin.test.assertEquals

class AddTwoNumbersTest {
    @Test
    fun addsSingleDigitNumbersWithoutCarry() {
        val node1 = LinkedList<Int>().apply { add(4) }
        val node2 = LinkedList<Int>().apply { add(4) }

        val actual = AddTwoNumbers.add(node1, node2)

        assertEquals(listOf(8), actual)
    }

    @Test
    fun addsSingleDigitNumbersWithCarry() {
        val node1 = LinkedList<Int>().apply { add(4) }
        val node2 = LinkedList<Int>().apply { add(8) }

        val actual = AddTwoNumbers.add(node1, node2)

        assertEquals(listOf(2, 1), actual)
    }

    @Test
    fun addsSameLengthNumbersWithIntermediateCarry() {
        val node1 =
            LinkedList<Int>().apply {
                add(2)
                add(4)
                add(3)
            }
        val node2 =
            LinkedList<Int>().apply {
                add(5)
                add(6)
                add(4)
            }

        val actual = AddTwoNumbers.add(node1, node2)

        assertEquals(listOf(7, 0, 8), actual)
    }

    @Test
    fun returnsZeroWhenBothInputsAreZero() {
        val node1 = LinkedList<Int>().apply { add(0) }
        val node2 = LinkedList<Int>().apply { add(0) }

        val actual = AddTwoNumbers.add(node1, node2)

        assertEquals(listOf(0), actual)
    }

    @Test
    fun addsDifferentLengthNumbersWithLongCarryChain() {
        val node1 =
            LinkedList<Int>().apply {
                add(9)
                add(9)
                add(9)
                add(9)
                add(9)
                add(9)
                add(9)
            }
        val node2 =
            LinkedList<Int>().apply {
                add(9)
                add(9)
                add(9)
                add(9)
            }

        val actual = AddTwoNumbers.add(node1, node2)

        assertEquals(listOf(8, 9, 9, 9, 0, 0, 0, 1), actual)
    }
}
