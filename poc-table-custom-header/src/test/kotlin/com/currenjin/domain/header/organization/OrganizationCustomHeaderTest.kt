package com.currenjin.header.organization

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrganizationCustomHeaderTest {
    @Test
    fun create() {
        val template =
            OrganizationCustomHeader(
                organizationId = 1L,
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
            OrganizationCustomHeader(
                organizationId = 1L,
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
            OrganizationCustomHeader(
                organizationId = 1L,
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
            OrganizationCustomHeader(
                organizationId = 1L,
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
            OrganizationCustomHeader(
                organizationId = 0L,
                tableName = "오더관리",
                columnName = "지급운임",
                sequence = 1,
                isVisible = true,
            )
        }
    }
}
