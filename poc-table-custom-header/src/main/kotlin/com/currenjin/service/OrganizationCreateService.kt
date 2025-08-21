package com.currenjin.service

import com.currenjin.header.organization.OrganizationCustomHeader

interface OrganizationCreateService {
    fun create(organizationCustomHeader: OrganizationCustomHeader): OrganizationCustomHeader
}
