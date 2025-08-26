package com.currenjin.domain.table

interface CustomHeaderPolicy {
    val tableName: String
    val columnName: String
    val sequence: Int
    val isVisible: Boolean
}