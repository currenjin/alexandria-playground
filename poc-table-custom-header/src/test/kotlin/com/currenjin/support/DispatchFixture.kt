package com.currenjin.support

import com.currenjin.domain.dispatch.Dispatch

class DispatchFixture {
    companion object {
        fun create(): Dispatch =
            Dispatch(
                id = Dispatch.DispatchId(1L),
                groupId = Dispatch.GroupId(1L),
                dispatchNumber = "D-123123",
                number = "123",
            )
    }
}
