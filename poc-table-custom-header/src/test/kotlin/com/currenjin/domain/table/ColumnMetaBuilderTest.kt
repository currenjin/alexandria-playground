package com.currenjin.domain.table

import com.currenjin.domain.header.columns.OrderColumns
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ColumnMetaBuilderTest {
    @Test
    fun buildColumnMeta_withoutPolicy() {
        val columns = OrderColumns.default
        val policy = emptyMap<String, Pair<Int, Boolean>>()
        val labelProvider = { key: String -> "Label_$key" }

        val result = ColumnMetaBuilder.buildColumnMeta(columns, policy, labelProvider)

        assertEquals(6, result.size)
        assertEquals("id", result[0].key)
        assertEquals("Label_id", result[0].label)
        assertEquals(true, result[0].visible)
        assertEquals(1, result[0].sequence)
    }

    @Test
    fun buildColumnMeta_withPolicy() {
        val columns = OrderColumns.default
        val policy =
            mapOf(
                "buyerName" to (10 to false),
                "orderCode" to (1 to true),
            )
        val labelProvider = { key: String -> "Label_$key" }

        val result = ColumnMetaBuilder.buildColumnMeta(columns, policy, labelProvider)

        val buyerNameMeta = result.first { it.key == "buyerName" }
        assertEquals(false, buyerNameMeta.visible)
        assertEquals(10, buyerNameMeta.sequence)

        val orderCodeMeta = result.first { it.key == "orderCode" }
        assertEquals(true, orderCodeMeta.visible)
        assertEquals(1, orderCodeMeta.sequence)
    }

    @Test
    fun buildColumnMeta_sortedBySequence() {
        val columns = OrderColumns.default
        val policy =
            mapOf(
                "orderNumber" to (1 to true),
                "id" to (2 to true),
                "buyerName" to (3 to true),
                "buyerId" to (4 to true),
            )
        val labelProvider = { key: String -> "Label_$key" }

        val result = ColumnMetaBuilder.buildColumnMeta(columns, policy, labelProvider)

        assertEquals("orderNumber", result[0].key)
        assertEquals("id", result[1].key)
        assertEquals("buyerName", result[2].key)
        assertEquals("buyerId", result[3].key)
    }
}
