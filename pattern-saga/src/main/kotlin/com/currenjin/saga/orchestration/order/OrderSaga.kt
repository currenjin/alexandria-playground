package com.currenjin.saga.orchestration.order

import com.currenjin.saga.core.SagaOrchestrator
import com.currenjin.saga.core.SagaResult

data class OrderContext(
    val orderId: String,
    val amount: Long,
    var orderCreated: Boolean = false,
    var inventoryReserved: Boolean = false,
    var paymentProcessed: Boolean = false,
    var orderConfirmed: Boolean = false,
    var shouldFailAt: String? = null,
    val compensationLog: MutableList<String> = mutableListOf(),
)

class OrderSaga {
    private val orchestrator = SagaOrchestrator(
        steps = listOf(
            CreateOrderStep(),
            ReserveInventoryStep(),
            ProcessPaymentStep(),
            ConfirmOrderStep(),
        )
    )

    val log get() = orchestrator.log

    fun execute(context: OrderContext): SagaResult {
        return orchestrator.execute(context)
    }
}
