package com.currenjin.tdd

class Postfix {
    companion object {
        fun calculate(postfix: String): Int {
            if (postfix.isBlank()) {
                throw IllegalArgumentException("postfix must not be blank")
            }

            val list = postfix.split(" ")
            return when (list.last()) {
                "+" -> list[0].toInt() + list[1].toInt()
                "-" -> list[0].toInt() - list[1].toInt()
                "*" -> list[0].toInt() * list[1].toInt()
                "/" -> list[0].toInt() / list[1].toInt()
                else -> list.last().toInt()
            }
        }
    }
}
