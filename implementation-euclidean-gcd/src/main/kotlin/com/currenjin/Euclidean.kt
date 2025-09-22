package com.currenjin

import kotlin.math.abs

object Euclidean {
    /**
     * Calculates the greatest common divisor (GCD) of two numbers using the Euclidean algorithm.
     *
     * @param n1 first integer
     * @param n2 second integer
     * @return greatest common divisor of n1 and n2
     */
    fun gcd(
        n1: Int,
        n2: Int,
    ): Int = gcd(n1.toLong(), n2.toLong()).toInt()

    /**
     * Calculates the greatest common divisor (GCD) of two numbers using the Euclidean algorithm.
     *
     * @param n1 first long
     * @param n2 second long
     * @return greatest common divisor of n1 and n2
     */
    fun gcd(
        n1: Long,
        n2: Long,
    ): Long {
        val x = abs(n1)
        val y = abs(n2)
        if (y == 0L) return x
        return gcd(y, x % y)
    }
}
