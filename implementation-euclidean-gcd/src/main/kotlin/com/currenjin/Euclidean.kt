package com.currenjin

class Euclidean {
    companion object {
        fun gcd(
            n1: Int,
            n2: Int,
        ): Int {
            if (n2 == 0) return n1
            return gcd(n2, n1 % n2)
        }
    }
}
