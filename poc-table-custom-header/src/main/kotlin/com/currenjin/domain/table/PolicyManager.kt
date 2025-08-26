package com.currenjin.domain.table

object PolicyManager {
    fun buildPolicy(
        table: String,
        customHeaderPolicies: List<CustomHeaderPolicy>,
    ): Map<String, Pair<Int, Boolean>> =
        customHeaderPolicies
            .filter { it.tableName == table }
            .associate { it.columnName to (it.sequence to it.isVisible) }
}