package com.currenjin.stringcalculator

class StringCalculator {
    fun add(numbers: String): Int {
        if (numbers.isEmpty()) return 0
        val nums = numbers.split(",", "\n").map { it.toInt() }
        val negatives = nums.filter { it < 0 }
        if (negatives.isNotEmpty()) {
            throw IllegalArgumentException("negatives not allowed: ${negatives.joinToString(", ")}")
        }
        return nums.sum()
    }
}