package com.currenjin.domain.header.organization

import com.currenjin.domain.table.CustomHeaderPolicy

data class OrganizationCustomHeader(
    val organizationId: Long,
    override val tableName: String,
    override val columnName: String,
    override val sequence: Int,
    override val isVisible: Boolean,
) : CustomHeaderPolicy {
    init {
        require(tableName.isNotBlank()) { "tableName must not be blank" }
        require(columnName.isNotBlank()) { "columnName must not be blank" }
        require(sequence > 0) { "sequence must be greater than 0" }
        require(organizationId > 0) { "organizationId must be greater than 0" }
    }
}
