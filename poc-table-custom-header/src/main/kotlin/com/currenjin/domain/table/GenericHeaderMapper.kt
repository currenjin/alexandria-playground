package com.currenjin.domain.table

import com.currenjin.header.organization.OrganizationCustomHeader
import java.time.LocalDate

object GenericHeaderMapper {
    fun <T> mapList(
        entities: List<T>,
        columns: List<Column<T>>,
        tableName: String,
        orgHeaders: List<OrganizationCustomHeader>,
        includeHiddenInRows: Boolean = true,
        labelProvider: (String) -> String = { it },
        formatter: (String, Any?) -> Any? = { _, v -> normalize(v) },
    ): TableResponse {
        val policy = buildPolicy(tableName, orgHeaders)

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

        return TableResponse(
            table = tableName,
            columns = metas,
            rows = rows,
        )
    }

    private fun buildPolicy(
        table: String,
        org: List<OrganizationCustomHeader>,
    ): Map<String, Pair<Int, Boolean>> =
        org
            .filter { it.tableName == table }
            .associate { it.columnName to (it.sequence to it.isVisible) }

    private fun normalize(v: Any?): Any? =
        when (v) {
            is LocalDate -> v.toString()
            else -> v
        }
}
