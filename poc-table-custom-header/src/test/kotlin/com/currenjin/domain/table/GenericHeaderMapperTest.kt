package com.currenjin.domain.table

import com.currenjin.domain.header.organization.OrganizationCustomHeader
import com.currenjin.domain.order.Order
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertTrue

class GenericHeaderMapperTest {
    @Test
    fun map_by_order_value() {
        val order =
            Order(
                id = Order.OrderId(1),
                buyerId = Order.BuyerId(1),
                buyerName = "거래처",
                receivedDate = LocalDate.of(2025, 5, 24),
                orderCode = "R-123-123",
                orderNumber = "O-123-123",
            )

        val result =
            GenericHeaderMapper.mapList(
                entities = listOf(order),
                columns = OrderColumns.default,
                tableName = Tables.ORDERS,
                organizationCustomHeaderList = emptyList(),
            )

        val keys = result.columns.sortedBy { it.sequence }.map { it.key }
        assertEquals(
            listOf("id", "buyerId", "buyerName", "receivedDate", "orderCode", "orderNumber"),
            keys,
        )
        assertEquals(true, result.columns.all { it.visible })

        val row = result.rows.first()
        assertEquals(1L, row["id"])
        assertEquals(1L, row["buyerId"])
        assertEquals("거래처", row["buyerName"])
        assertEquals("2025-05-24", row["receivedDate"])
        assertEquals("R-123-123", row["orderCode"])
        assertEquals("O-123-123", row["orderNumber"])
    }

    @Test
    fun visible_by_organization_custom_header() {
        val order = Order(id = Order.OrderId(1), buyerName = "세방")
        val orgHeaders =
            listOf(
                OrganizationCustomHeader(
                    organizationId = 10L,
                    tableName = Tables.ORDERS,
                    columnName = "buyerName",
                    sequence = 3,
                    isVisible = false,
                ),
            )

        val result =
            GenericHeaderMapper.mapList(
                entities = listOf(order),
                columns = OrderColumns.default,
                tableName = Tables.ORDERS,
                organizationCustomHeaderList = orgHeaders,
            )

        val meta = result.columns.first { it.key == "buyerName" }
        assertEquals(false, meta.visible)
    }

    @Test
    fun sort_by_sequence() {
        val order = Order(id = Order.OrderId(1), orderCode = "R-1", orderNumber = "O-1")

        val orgHeaders =
            listOf(
                OrganizationCustomHeader(10, Tables.ORDERS, "orderNumber", sequence = 1, isVisible = true),
                OrganizationCustomHeader(10, Tables.ORDERS, "orderCode", sequence = 2, isVisible = true),
                OrganizationCustomHeader(10, Tables.ORDERS, "id", sequence = 3, isVisible = true),
            )

        val result =
            GenericHeaderMapper.mapList(
                entities = listOf(order),
                columns = OrderColumns.default,
                tableName = Tables.ORDERS,
                organizationCustomHeaderList = orgHeaders,
            )

        val orderedKeys = result.columns.sortedBy { it.sequence }.map { it.key }
        val idxOrderNumber = orderedKeys.indexOf("orderNumber")
        val idxOrderCode = orderedKeys.indexOf("orderCode")
        val idxId = orderedKeys.indexOf("id")

        assertTrue(
            (idxOrderNumber < idxOrderCode) &&
                (idxOrderCode < idxId),
        )
    }

    @Test
    fun remove_invisible_rows() {
        val order = Order(id = Order.OrderId(1), buyerName = "세방")
        val orgHeaders =
            listOf(
                OrganizationCustomHeader(
                    organizationId = 10,
                    tableName = Tables.ORDERS,
                    columnName = "buyerName",
                    sequence = 2,
                    isVisible = false,
                ),
            )

        val result =
            GenericHeaderMapper.mapList(
                entities = listOf(order),
                columns = OrderColumns.default,
                tableName = Tables.ORDERS,
                organizationCustomHeaderList = orgHeaders,
                includeHiddenInRows = false,
            )

        val row = result.rows.first()

        assertEquals(false, result.columns.first { it.key == "buyerName" }.visible)
        check(!row.containsKey("buyerName")) { "숨김 컬럼이 rows에 포함되면 안 됨" }
    }
}
