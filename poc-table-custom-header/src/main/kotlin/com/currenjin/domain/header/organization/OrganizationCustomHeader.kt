package com.currenjin.header.organization

data class OrganizationCustomHeader(
    val organizationId: Long,
    val tableName: String,
    val columnName: String,
    val sequence: Int,
    val isVisible: Boolean,
) {
    init {
        require(tableName.isNotBlank()) { "tableName must not be blank" }
        require(columnName.isNotBlank()) { "columnName must not be blank" }
        require(sequence > 0) { "sequence must be greater than 0" }
        require(organizationId > 0) { "organizationId must be greater than 0" }
    }
}
