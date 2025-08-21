package com.currenjin.domain.table

import com.currenjin.domain.header.organization.OrganizationCustomHeader
import java.time.LocalDate

object GenericHeaderMapper {
    fun <T> mapList(
        entities: List<T>,
        columns: List<Column<T>>,
        tableName: String,
        organizationCustomHeaderList: List<OrganizationCustomHeader>,
        includeHiddenInRows: Boolean = true,
        labelProvider: (String) -> String = { it },
        formatter: (String, Any?) -> Any? = { _, value -> normalize(value) },
    ): TableResult {
        val policy = buildPolicy(tableName, organizationCustomHeaderList)

        val metas =
            columns
                .mapIndexed { idx, c ->
                    val (seq, vis) = policy[c.key] ?: (idx + 1 to true)
                    ColumnMeta(
                        key = c.key,
                        label = labelProvider(c.key),
                        visible = vis,
                        sequence = seq,
                    )
                }.sortedBy { it.sequence }

        val rows =
            entities.map { e ->
                val row = LinkedHashMap<String, Any?>()
                metas.forEach { meta ->
                    if (includeHiddenInRows || meta.visible) {
                        val col = columns.first { it.key == meta.key }
                        row[meta.key] = formatter(meta.key, col.extractor(e))
                    }
                }
                row
            }

        return TableResult(
            table = tableName,
            columns = metas,
            rows = rows,
        )
    }

    private fun buildPolicy(
        table: String,
        organizationCustomHeaderList: List<OrganizationCustomHeader>,
    ): Map<String, Pair<Int, Boolean>> =
        organizationCustomHeaderList
            .filter { it.tableName == table }
            .associate { it.columnName to (it.sequence to it.isVisible) }

    private fun normalize(value: Any?): Any? =
        when (value) {
            is LocalDate -> value.toString()
            else -> value
        }
}
