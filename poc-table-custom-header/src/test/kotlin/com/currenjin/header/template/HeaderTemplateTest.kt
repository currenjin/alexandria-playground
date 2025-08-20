package com.currenjin.header.template

import org.junit.jupiter.api.Test
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
}
