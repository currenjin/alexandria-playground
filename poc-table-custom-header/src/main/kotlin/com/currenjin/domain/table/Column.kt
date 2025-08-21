package com.currenjin.domain.table

interface Column<T> {
    val key: String
    val extractor: (T) -> Any?
}
