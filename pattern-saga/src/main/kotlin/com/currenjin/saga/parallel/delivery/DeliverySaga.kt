package com.currenjin.saga.parallel.delivery

import com.currenjin.saga.core.ParallelSagaOrchestrator
import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.StepGroup

data class DeliveryContext(
    val orderId: String,
    var orderPlaced: Boolean = false,
    var foodPrepared: Boolean = false,
    var driverAssigned: Boolean = false,
    var delivered: Boolean = false,
    var shouldFailAt: String? = null,
    val compensationLog: MutableList<String> = mutableListOf(),
)

class DeliverySaga {
    private val orchestrator = ParallelSagaOrchestrator(
        groups = listOf(
            StepGroup(listOf(PlaceOrderStep())),
            StepGroup(listOf(PrepareFoodStep(), AssignDriverStep())),
            StepGroup(listOf(DeliverStep())),
        )
    )

    val log get() = orchestrator.log

    fun execute(context: DeliveryContext): SagaResult {
        return orchestrator.execute(context)
    }
}
