package com.currenjin.header.organization

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrganizationCustomHeaderTest {
    @Test
    fun create() {
        val template =
            OrganizationCustomHeader(
                tableName = "오더관리",
                columnName = "지급운임",
                sequence = 1,
            )

        assertEquals("지급운임", template.columnName)
        assertEquals("오더관리", template.tableName)
        assertEquals(1, template.sequence)
    }

    @Test
    fun create_throwsException_whenEmptyOrBlankTableName() {
        assertThrows<IllegalArgumentException> {
            OrganizationCustomHeader(
                tableName = "",
                columnName = "지급운임",
                sequence = 1,
            )
        }
    }

    @Test
    fun create_throwsException_whenEmptyOrBlankColumnName() {
        assertThrows<IllegalArgumentException> {
            OrganizationCustomHeader(
                tableName = "오더관리",
                columnName = "",
                sequence = 1,
            )
        }
    }

    @Test
    fun create_throwsException_whenZeroSequence() {
        assertThrows<IllegalArgumentException> {
            OrganizationCustomHeader(
                tableName = "오더관리",
                columnName = "지급운임",
                sequence = 0,
            )
        }
    }
}
