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
}
