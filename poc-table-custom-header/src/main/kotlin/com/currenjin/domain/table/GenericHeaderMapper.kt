package com.currenjin.domain.table

import java.time.LocalDate

object GenericHeaderMapper {
    /** 헤더와 값 매핑 */
    fun <T> mapList(
        entities: List<T>,
        columns: List<Column<T>>,
        tableName: String,
        customHeaderPolicies: List<CustomHeaderPolicy>,
        includeHiddenInRows: Boolean = true,
        labelProvider: (String) -> String = { it },
        formatter: (String, Any?) -> Any? = { _, value -> normalize(value) },
    ): TableResult {
        val columnsByKey: Map<String, Column<T>> = columns.associateBy { it.key }
        require(columnsByKey.size == columns.size) { "Duplicate column keys are not allowed." }

        val policy: Map<String, Pair<Int, Boolean>> =
            buildPolicy(
                table = tableName,
                customHeaderPolicies = customHeaderPolicies,
            )

        val metas: List<ColumnMeta> =
            buildColumnMeta(
                columns = columns,
                policy = policy,
                labelProvider = labelProvider,
            )

        val rows: List<Map<String, Any?>> =
            buildRows(
                entities = entities,
                metas = metas,
                columnsByKey = columnsByKey,
                includeHiddenInRows = includeHiddenInRows,
                formatter = formatter,
            )

        return TableResult(
            table = tableName,
            columns = metas,
            rows = rows,
        )
    }

    /** 정책 설정: 나중에 들어온 정책이 우선순위를 가짐. */
    private fun buildPolicy(
        table: String,
        customHeaderPolicies: List<CustomHeaderPolicy>,
    ): Map<String, Pair<Int, Boolean>> =
        customHeaderPolicies
            .filter { it.tableName == table }
            .associate { it.columnName to (it.sequence to it.isVisible) }

    /** 컬럼 메타 구성: 정책 적용 후 sequence 오름차순 정렬. */
    private fun <T> buildColumnMeta(
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

    /** 행 생성: meta 순서에 따라 값을 채우고, 필요 시 숨김 컬럼 제외. */
    private fun <T> buildRows(
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

    private fun normalize(value: Any?): Any? =
        when (value) {
            is LocalDate -> value.toString()
            else -> value
        }
}
