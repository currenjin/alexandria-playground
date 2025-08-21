package com.currenjin.domain.table.columns

import com.currenjin.domain.dispatch.Dispatch
import com.currenjin.domain.table.Column

enum class DispatchColumns(
    override val key: String,
    override val extractor: (Dispatch) -> Any?,
) : Column<Dispatch> {
    ID("id", { it.id.value }),
    GROUP_ID("groupId", { it.groupId.value }),
    DISPATCH_NUMBER("dispatchNumber", { it.dispatchNumber }),
    NUMBER("number", { it.number }),
    ;

    companion object {
        val default = entries.toList()
    }
}
