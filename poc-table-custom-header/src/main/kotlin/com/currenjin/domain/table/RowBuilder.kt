package com.currenjin.domain.table

object RowBuilder {
    fun <T> buildRows(
        entities: List<T>,
        metas: List<ColumnMeta>,
        columnsByKey: Map<String, Column<T>>,
        includeHiddenInRows: Boolean,
        formatter: (String, Any?) -> Any?,
    ): List<Map<String, Any?>> =
        entities.map { entity ->
            val row = LinkedHashMap<String, Any?>(metas.size)
            metas.forEach { meta ->
                if (includeHiddenInRows || meta.visible) {
                    val value = columnsByKey.getValue(meta.key).extractor(entity)
                    row[meta.key] = formatter(meta.key, value)
                }
            }
            row
        }
}