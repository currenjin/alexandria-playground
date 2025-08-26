package com.currenjin.domain.table

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DataNormalizerTest {
    @Test
    fun normalize_localDate() {
        val date = LocalDate.of(2023, 12, 25)

        val result = DataNormalizer.normalize(date)

        assertEquals("2023-12-25", result)
    }

    @Test
    fun normalize_string() {
        val value = "test string"

        val result = DataNormalizer.normalize(value)

        assertEquals("test string", result)
    }

    @Test
    fun normalize_number() {
        val value = 123

        val result = DataNormalizer.normalize(value)

        assertEquals(123, result)
    }

    @Test
    fun normalize_null() {
        val result = DataNormalizer.normalize(null)

        assertEquals(null, result)
    }

    @Test
    fun normalize_boolean() {
        val value = true

        val result = DataNormalizer.normalize(value)

        assertEquals(true, result)
    }
}