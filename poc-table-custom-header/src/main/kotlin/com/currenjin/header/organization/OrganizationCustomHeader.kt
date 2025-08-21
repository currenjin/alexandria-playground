package com.currenjin.header.organization

data class OrganizationCustomHeader(
    val tableName: String,
    val columnName: String,
    val sequence: Int,
) {
    init {
        require(tableName.isNotBlank()) { "tableName must not be blank" }
        require(columnName.isNotBlank()) { "columnName must not be blank" }
        require(sequence > 0) { "sequence must be greater than 0" }
    }
}
