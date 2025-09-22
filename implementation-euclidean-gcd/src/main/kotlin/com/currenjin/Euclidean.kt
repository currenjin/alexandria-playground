package com.currenjin

import kotlin.math.abs

class Euclidean {
    companion object {
        fun gcd(
            n1: Int,
            n2: Int,
        ): Int {
            val x = abs(n1)
            val y = abs(n2)
            if (y == 0) return x
            return gcd(y, x % y)
        }
    }
}
