package com.currenjin.service

import com.currenjin.domain.header.organization.OrganizationCustomHeader

interface OrganizationCustomHeaderReader {
    fun readById(organizationId: Long): List<OrganizationCustomHeader>

    fun readByIdAndTableName(
        organizationId: Long,
        tableName: String,
    ): List<OrganizationCustomHeader>
}
