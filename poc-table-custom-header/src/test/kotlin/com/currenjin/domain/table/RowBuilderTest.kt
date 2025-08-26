package com.currenjin.domain.table

import com.currenjin.domain.header.columns.OrderColumns
import com.currenjin.support.OrderFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class RowBuilderTest {
    @Test
    fun buildRows_allVisible() {
        val order = OrderFixture.create()
        val entities = listOf(order)
        val metas = listOf(
            ColumnMeta("id", "ID", true, 1),
            ColumnMeta("buyerName", "구매자명", true, 2)
        )
        val columnsByKey = mapOf(
            "id" to OrderColumns.ID,
            "buyerName" to OrderColumns.BUYER_NAME
        )
        val formatter = { _: String, value: Any? -> value }

        val result = RowBuilder.buildRows(entities, metas, columnsByKey, true, formatter)

        assertEquals(1, result.size)
        val row = result[0]
        assertEquals(order.id.value, row["id"])
        assertEquals(order.buyerName, row["buyerName"])
    }

    @Test
    fun buildRows_withHiddenColumns_includeHidden() {
        val order = OrderFixture.create()
        val entities = listOf(order)
        val metas = listOf(
            ColumnMeta("id", "ID", true, 1),
            ColumnMeta("buyerName", "구매자명", false, 2)
        )
        val columnsByKey = mapOf(
            "id" to OrderColumns.ID,
            "buyerName" to OrderColumns.BUYER_NAME
        )
        val formatter = { _: String, value: Any? -> value }

        val result = RowBuilder.buildRows(entities, metas, columnsByKey, true, formatter)

        assertEquals(1, result.size)
        val row = result[0]
        assertTrue(row.containsKey("id"))
        assertTrue(row.containsKey("buyerName"))
    }

    @Test
    fun buildRows_withHiddenColumns_excludeHidden() {
        val order = OrderFixture.create()
        val entities = listOf(order)
        val metas = listOf(
            ColumnMeta("id", "ID", true, 1),
            ColumnMeta("buyerName", "구매자명", false, 2)
        )
        val columnsByKey = mapOf(
            "id" to OrderColumns.ID,
            "buyerName" to OrderColumns.BUYER_NAME
        )
        val formatter = { _: String, value: Any? -> value }

        val result = RowBuilder.buildRows(entities, metas, columnsByKey, false, formatter)

        assertEquals(1, result.size)
        val row = result[0]
        assertTrue(row.containsKey("id"))
        assertFalse(row.containsKey("buyerName"))
    }

    @Test
    fun buildRows_withFormatter() {
        val order = OrderFixture.create()
        val entities = listOf(order)
        val metas = listOf(
            ColumnMeta("id", "ID", true, 1)
        )
        val columnsByKey = mapOf(
            "id" to OrderColumns.ID
        )
        val formatter = { _: String, value: Any? -> "formatted_$value" }

        val result = RowBuilder.buildRows(entities, metas, columnsByKey, true, formatter)

        assertEquals(1, result.size)
        val row = result[0]
        assertEquals("formatted_${order.id.value}", row["id"])
    }
}