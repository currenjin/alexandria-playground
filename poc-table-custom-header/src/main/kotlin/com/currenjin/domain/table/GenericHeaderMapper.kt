package com.currenjin.domain.table

object GenericHeaderMapper {
    fun <T> mapList(
        entities: List<T>,
        columns: List<Column<T>>,
        tableName: String,
        customHeaderPolicies: List<CustomHeaderPolicy>,
        includeHiddenInRows: Boolean = true,
        labelProvider: (String) -> String = { it },
        formatter: (String, Any?) -> Any? = { _, value -> DataNormalizer.normalize(value) },
    ): TableResult {
        val columnsByKey: Map<String, Column<T>> = columns.associateBy { it.key }
        require(columnsByKey.size == columns.size) { "Duplicate column keys are not allowed." }

        val policy: Map<String, Pair<Int, Boolean>> =
            PolicyManager.buildPolicy(
                table = tableName,
                customHeaderPolicies = customHeaderPolicies,
            )

        val metas: List<ColumnMeta> =
            ColumnMetaBuilder.buildColumnMeta(
                columns = columns,
                policy = policy,
                labelProvider = labelProvider,
            )

        val rows: List<Map<String, Any?>> =
            RowBuilder.buildRows(
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
}
