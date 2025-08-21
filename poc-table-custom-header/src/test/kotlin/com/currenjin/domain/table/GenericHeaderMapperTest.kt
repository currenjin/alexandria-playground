package com.currenjin.domain.table

import com.currenjin.domain.header.organization.OrganizationCustomHeader
import com.currenjin.support.OrderFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class GenericHeaderMapperTest {
    @Test
    fun map_by_order_value() {
        val order = OrderFixture.create()

        val result =
            GenericHeaderMapper.mapList(
                entities = listOf(order),
                columns = OrderColumns.default,
                tableName = Tables.ORDERS,
                organizationCustomHeaderList = emptyList(),
                userCustomHeaderList = emptyList(),
            )

        val keys = result.columns.sortedBy { it.sequence }.map { it.key }
        assertEquals(
            listOf("id", "buyerId", "buyerName", "receivedDate", "orderCode", "orderNumber"),
            keys,
        )
        assertEquals(true, result.columns.all { it.visible })

        val row = result.rows.first()
        assertEquals(order.id.value, row["id"])
        assertEquals(order.buyerId?.value, row["buyerId"])
        assertEquals(order.buyerName, row["buyerName"])
        assertEquals(order.receivedDate.toString(), row["receivedDate"])
        assertEquals(order.orderCode, row["orderCode"])
        assertEquals(order.orderNumber, row["orderNumber"])
    }

    @Test
    fun visible_by_organization_custom_header() {
        val order = OrderFixture.create()
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
                userCustomHeaderList = emptyList(),
            )

        val meta = result.columns.first { it.key == "buyerName" }
        assertEquals(false, meta.visible)
    }

    @Test
    fun sort_by_sequence() {
        val order = OrderFixture.create()

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
                userCustomHeaderList = emptyList(),
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
    fun invisible_by_organization_custom_header() {
        val order = OrderFixture.create()
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
                userCustomHeaderList = emptyList(),
                includeHiddenInRows = false,
            )

        assertEquals(false, result.columns.first { it.key == "buyerName" }.visible)
    }
}
