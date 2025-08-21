package com.currenjin.domain.dispatch

data class Dispatch(
    val id: DispatchId,
    val groupId: GroupId,
    val dispatchNumber: String,
    val number: String? = null,
) {
    data class DispatchId(
        val value: Long,
    ) {
        init {
            require(value > 0)
        }
    }

    data class GroupId(
        val value: Long,
    ) {
        init {
            require(value > 0)
        }
    }
}
