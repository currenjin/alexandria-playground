package com.currenjin

import kotlin.math.abs

object Euclidean {
    fun gcd(
        n1: Int,
        n2: Int,
    ): Int = gcd(n1.toLong(), n2.toLong()).toInt()

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
