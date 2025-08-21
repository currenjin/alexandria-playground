package com.currenjin.domain.header.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserCustomHeaderTest {
    @Test
    fun create() {
        val template =
            UserCustomHeader(
                userId = 1L,
                tableName = "오더관리",
                columnName = "지급운임",
                sequence = 1,
                isVisible = true,
            )

        assertEquals("지급운임", template.columnName)
        assertEquals("오더관리", template.tableName)
        assertEquals(1, template.sequence)
    }

    @Test
    fun create_throwsException_whenEmptyOrBlankTableName() {
        assertThrows<IllegalArgumentException> {
            UserCustomHeader(
                userId = 1L,
                tableName = "",
                columnName = "지급운임",
                sequence = 1,
                isVisible = false,
            )
        }
    }

    @Test
    fun create_throwsException_whenEmptyOrBlankColumnName() {
        assertThrows<IllegalArgumentException> {
            UserCustomHeader(
                userId = 1L,
                tableName = "오더관리",
                columnName = "",
                sequence = 1,
                isVisible = true,
            )
        }
    }

    @Test
    fun create_throwsException_whenZeroSequence() {
        assertThrows<IllegalArgumentException> {
            UserCustomHeader(
                userId = 1L,
                tableName = "오더관리",
                columnName = "지급운임",
                sequence = 0,
                isVisible = true,
            )
        }
    }

    @Test
    fun create_throwsException_whenZeroOrganizationId() {
        assertThrows<IllegalArgumentException> {
            UserCustomHeader(
                userId = 0L,
                tableName = "오더관리",
                columnName = "지급운임",
                sequence = 1,
                isVisible = true,
            )
        }
    }
}
