package com.currenjin.domain.header.template

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class HeaderTemplateTest {
    @Test
    fun create() {
        val template =
            HeaderTemplate(
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
            HeaderTemplate(
                tableName = "",
                columnName = "지급운임",
                sequence = 1,
            )
        }
    }

    @Test
    fun create_throwsException_whenEmptyOrBlankColumnName() {
        assertThrows<IllegalArgumentException> {
            HeaderTemplate(
                tableName = "오더관리",
                columnName = "",
                sequence = 1,
            )
        }
    }

    @Test
    fun create_throwsException_whenZeroSequence() {
        assertThrows<IllegalArgumentException> {
            HeaderTemplate(
                tableName = "오더관리",
                columnName = "지급운임",
                sequence = 0,
            )
        }
    }
}
