package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger

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

    @Test
    fun gcd_of_0_and_5_is_5() {
        assertEquals(5, Euclidean.gcd(0, 5))
    }

    @Test
    fun gcd_of_7_and_0_is_7() {
        assertEquals(7, Euclidean.gcd(7, 0))
    }

    @Test
    fun gcd_of_minus3_and_2_is_1() {
        assertEquals(1, Euclidean.gcd(-3, 2))
    }

    @Test
    fun gcd_of_minus4_and_minus2_is_2() {
        assertEquals(2, Euclidean.gcd(-4, -2))
    }

    @Test
    fun gcd_large_numbers() {
        assertEquals(10_000L, Euclidean.gcd(100_000L, 10_000L))
    }

    @Test
    fun gcd_big_integers() {
        val n1 = BigInteger("123456789123456789123456789")
        val n2 = BigInteger("987654321")

        assertEquals(BigInteger("9"), Euclidean.gcd(n1, n2))
    }
}
