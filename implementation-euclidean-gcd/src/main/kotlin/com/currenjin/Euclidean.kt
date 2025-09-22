package com.currenjin

import kotlin.math.abs

class Euclidean {
    companion object {
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
}
