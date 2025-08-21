package com.currenjin.service

import com.currenjin.header.organization.OrganizationCustomHeader

interface OrganizationCustomHeaderReader {
    fun findById(organizationId: Long): List<OrganizationCustomHeader>
}
