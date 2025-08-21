package com.currenjin.service

import com.currenjin.support.OrderFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class OrganizationHeaderMatcherTest {
    @Mock
    private lateinit var organizationCustomHeaderReader: OrganizationCustomHeaderReader

    private lateinit var sut: OrganizationHeaderMatcher

    @BeforeEach
    fun setUp() {
        sut = OrganizationHeaderMatcher(organizationCustomHeaderReader)
    }

    @Test
    fun match() {
        val organizationId = 1L
        val tableName = "오더관리"
        val order = OrderFixture.create()

        val actual = sut.matchBy(organizationId, tableName, order)

        assertEquals(order.id, actual["id"])
        assertEquals(order.buyerId, actual["buyerId"])
        assertEquals(order.buyerName, actual["buyerName"])
        assertEquals(order.receivedDate, actual["receivedDate"])
        assertEquals(order.orderCode, actual["orderCode"])
        assertEquals(order.orderNumber, actual["orderNumber"])
    }
}
