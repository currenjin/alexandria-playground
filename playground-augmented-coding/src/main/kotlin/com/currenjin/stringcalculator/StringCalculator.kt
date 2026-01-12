package com.currenjin.stringcalculator

class StringCalculator {
    fun add(numbers: String): Int {
        if (numbers.isEmpty()) return 0

        val (delimiters, numbersToProcess) = parseDelimiters(numbers)
        val nums = numbersToProcess.split(*delimiters.toTypedArray()).map { it.toInt() }

        validateNoNegatives(nums)

        return nums.filter { it <= 1000 }.sum()
    }

    private fun validateNoNegatives(nums: List<Int>) {
        val negatives = nums.filter { it < 0 }
        if (negatives.isNotEmpty()) {
            throw IllegalArgumentException("negatives not allowed: ${negatives.joinToString(", ")}")
        }
    }

    private fun parseDelimiters(numbers: String): Pair<List<String>, String> {
        if (!numbers.startsWith("//")) {
            return Pair(listOf(",", "\n"), numbers)
        }
        val delimiterEnd = numbers.indexOf("\n")
        val delimiterPart = numbers.substring(2, delimiterEnd)
        val delimiters = Regex("\\[([^]]+)]").findAll(delimiterPart)
            .map { it.groupValues[1] }
            .toList()
        return Pair(delimiters, numbers.substring(delimiterEnd + 1))
    }
}