package com.currenjin.domain.table

import com.currenjin.domain.header.organization.OrganizationCustomHeader
import com.currenjin.domain.header.user.UserCustomHeader
import com.currenjin.domain.table.columns.DispatchColumns
import com.currenjin.domain.table.columns.OrderColumns
import com.currenjin.support.DispatchFixture
import com.currenjin.support.OrderFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class GenericHeaderMapperTest {
    @Test
    fun map_by_order_value() {
        val order = OrderFixture.create()

        val result =
            GenericHeaderMapper.mapList(
                entities = listOf(order, order),
                columns = OrderColumns.default,
                tableName = Tables.ORDERS,
                organizationCustomHeaderList = emptyList(),
                userCustomHeaderList = emptyList(),
            )

        val keys = result.columns.sortedBy { it.sequence }.map { it.key }
        assertEquals(
            listOf(
                OrderColumns.ID.key,
                OrderColumns.BUYER_ID.key,
                OrderColumns.BUYER_NAME.key,
                OrderColumns.RECEIVED_DATE.key,
                OrderColumns.ORDER_CODE.key,
                OrderColumns.ORDER_NUMBER.key,
            ),
            keys,
        )
        assertEquals(true, result.columns.all { it.visible })

        val row = result.rows.first()
        assertEquals(order.id.value, row[OrderColumns.ID.key])
        assertEquals(order.buyerId?.value, row[OrderColumns.BUYER_ID.key])
        assertEquals(order.buyerName, row[OrderColumns.BUYER_NAME.key])
        assertEquals(order.receivedDate.toString(), row[OrderColumns.RECEIVED_DATE.key])
        assertEquals(order.orderCode, row[OrderColumns.ORDER_CODE.key])
        assertEquals(order.orderNumber, row[OrderColumns.ORDER_NUMBER.key])
    }

    @Test
    fun map_by_dispatch_value() {
        val dispatch = DispatchFixture.create()

        val result =
            GenericHeaderMapper.mapList(
                entities = listOf(dispatch, dispatch),
                columns = DispatchColumns.default,
                tableName = Tables.ORDERS,
                organizationCustomHeaderList = emptyList(),
                userCustomHeaderList = emptyList(),
            )

        val keys = result.columns.sortedBy { it.sequence }.map { it.key }
        assertEquals(
            listOf(
                DispatchColumns.ID.key,
                DispatchColumns.GROUP_ID.key,
                DispatchColumns.DISPATCH_NUMBER.key,
                DispatchColumns.NUMBER.key,
            ),
            keys,
        )
        assertEquals(true, result.columns.all { it.visible })

        val row = result.rows.first()
        assertEquals(dispatch.id.value, row[DispatchColumns.ID.key])
        assertEquals(dispatch.groupId.value, row[DispatchColumns.GROUP_ID.key])
        assertEquals(dispatch.dispatchNumber, row[DispatchColumns.DISPATCH_NUMBER.key])
        assertEquals(dispatch.number, row[DispatchColumns.NUMBER.key])
    }

    @Test
    fun visible_by_organization_custom_header() {
        val order = OrderFixture.create()
        val orgHeaders =
            listOf(
                OrganizationCustomHeader(
                    organizationId = 10L,
                    tableName = Tables.ORDERS,
                    columnName = OrderColumns.BUYER_NAME.key,
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

        val meta = result.columns.first { it.key == OrderColumns.BUYER_NAME.key }
        assertEquals(false, meta.visible)
    }

    @Test
    fun sort_by_sequence() {
        val order = OrderFixture.create()

        val orgHeaders =
            listOf(
                OrganizationCustomHeader(10, Tables.ORDERS, OrderColumns.ORDER_NUMBER.key, sequence = 1, isVisible = true),
                OrganizationCustomHeader(10, Tables.ORDERS, OrderColumns.ORDER_CODE.key, sequence = 2, isVisible = true),
                OrganizationCustomHeader(10, Tables.ORDERS, OrderColumns.ID.key, sequence = 3, isVisible = true),
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
        val idxOrderNumber = orderedKeys.indexOf(OrderColumns.ORDER_NUMBER.key)
        val idxOrderCode = orderedKeys.indexOf(OrderColumns.ORDER_CODE.key)
        val idxId = orderedKeys.indexOf(OrderColumns.ID.key)

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
                    columnName = OrderColumns.BUYER_NAME.key,
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

        assertEquals(false, result.columns.first { it.key == OrderColumns.BUYER_NAME.key }.visible)
        assertFalse(result.rows.first().containsKey(OrderColumns.BUYER_NAME.key))
    }

    @Test
    fun organization_override_user_custom_header() {
        val order = OrderFixture.create()
        val orgHeaders =
            listOf(
                OrganizationCustomHeader(
                    organizationId = 10L,
                    tableName = Tables.ORDERS,
                    columnName = OrderColumns.BUYER_NAME.key,
                    sequence = 3,
                    isVisible = false,
                ),
            )
        val userHeaders =
            listOf(
                UserCustomHeader(
                    userId = 10L,
                    tableName = Tables.ORDERS,
                    columnName = OrderColumns.BUYER_NAME.key,
                    sequence = 3,
                    isVisible = true,
                ),
            )

        val result =
            GenericHeaderMapper.mapList(
                entities = listOf(order),
                columns = OrderColumns.default,
                tableName = Tables.ORDERS,
                organizationCustomHeaderList = orgHeaders,
                userCustomHeaderList = userHeaders,
            )

        val meta = result.columns.first { it.key == OrderColumns.BUYER_NAME.key }
        assertEquals(false, meta.visible)
    }
}
