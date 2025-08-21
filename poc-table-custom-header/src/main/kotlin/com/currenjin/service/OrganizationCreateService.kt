package com.currenjin.service

import com.currenjin.domain.header.organization.OrganizationCustomHeader

interface OrganizationCreateService {
    fun create(organizationCustomHeader: OrganizationCustomHeader): OrganizationCustomHeader
}
