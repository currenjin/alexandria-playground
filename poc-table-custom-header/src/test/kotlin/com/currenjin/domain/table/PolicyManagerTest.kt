package com.currenjin.domain.table

import com.currenjin.domain.header.organization.OrganizationCustomHeader
import com.currenjin.domain.header.user.UserCustomHeader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PolicyManagerTest {
    @Test
    fun buildPolicy_emptyPolicies() {
        val policies = emptyList<CustomHeaderPolicy>()

        val result = PolicyManager.buildPolicy("ORDERS", policies)

        assertEquals(emptyMap<String, Pair<Int, Boolean>>(), result)
    }

    @Test
    fun buildPolicy_singlePolicy() {
        val policies =
            listOf(
                OrganizationCustomHeader(
                    organizationId = 1L,
                    tableName = "ORDERS",
                    columnName = "buyerName",
                    sequence = 5,
                    isVisible = false,
                ),
            )

        val result = PolicyManager.buildPolicy("ORDERS", policies)

        assertEquals(mapOf("buyerName" to (5 to false)), result)
    }

    @Test
    fun buildPolicy_multiplePolicies() {
        val policies =
            listOf(
                OrganizationCustomHeader(1L, "ORDERS", "buyerName", 5, false),
                UserCustomHeader(1L, "ORDERS", "orderCode", 1, true),
                OrganizationCustomHeader(1L, "DISPATCHES", "number", 2, true),
            )

        val result = PolicyManager.buildPolicy("ORDERS", policies)

        assertEquals(
            mapOf(
                "buyerName" to (5 to false),
                "orderCode" to (1 to true),
            ),
            result,
        )
    }

    @Test
    fun buildPolicy_filterByTableName() {
        val policies =
            listOf(
                OrganizationCustomHeader(1L, "ORDERS", "buyerName", 5, false),
                OrganizationCustomHeader(1L, "DISPATCHES", "number", 2, true),
            )

        val result = PolicyManager.buildPolicy("DISPATCHES", policies)

        assertEquals(mapOf("number" to (2 to true)), result)
    }
}
