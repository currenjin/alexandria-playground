package com.currenjin.domain.table

object ColumnMetaBuilder {
    fun <T> buildColumnMeta(
        columns: List<Column<T>>,
        policy: Map<String, Pair<Int, Boolean>>,
        labelProvider: (String) -> String,
    ): List<ColumnMeta> =
        columns
            .mapIndexed { idx, c ->
                val (seq, visible) = policy[c.key] ?: (idx + 1 to true)
                ColumnMeta(
                    key = c.key,
                    label = labelProvider(c.key),
                    visible = visible,
                    sequence = seq,
                )
            }.sortedBy { it.sequence }
}