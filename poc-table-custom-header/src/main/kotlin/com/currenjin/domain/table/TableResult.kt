package com.currenjin.domain.table

data class TableResult(
    val table: String,
    val columns: List<ColumnMeta>,
    val rows: List<Map<String, Any?>>,
)
