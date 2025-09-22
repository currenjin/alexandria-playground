package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EuclideanTest {
    @Test
    fun gcd_of_1_and_1_is_1() {
        assertEquals(1, Euclidean.gcd(1, 1))
    }

    @Test
    fun gcd_of_2_and_1_is_1() {
        assertEquals(1, Euclidean.gcd(2, 1))
    }

    @Test
    fun gcd_of_2_and_2_is_2() {
        assertEquals(2, Euclidean.gcd(2, 2))
    }

    @Test
    fun gcd_of_3_and_2_is_1() {
        assertEquals(1, Euclidean.gcd(3, 2))
    }

    @Test
    fun gcd_of_4_and_2_is_2() {
        assertEquals(2, Euclidean.gcd(4, 2))
    }

    @Test
    fun gcd_of_9_and_6_is_3() {
        assertEquals(3, Euclidean.gcd(9, 6))
    }

    @Test
    fun gcd_of_12_and_18_is_6() {
        assertEquals(6, Euclidean.gcd(12, 18))
    }
}
