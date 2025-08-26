package com.currenjin.domain.table

import java.time.LocalDate

object DataNormalizer {
    fun normalize(value: Any?): Any? =
        when (value) {
            is LocalDate -> value.toString()
            else -> value
        }
}