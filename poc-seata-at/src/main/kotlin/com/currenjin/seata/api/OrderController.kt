package com.currenjin.seata.api

import com.currenjin.seata.order.OrderService
import com.currenjin.seata.order.PlaceOrderCommand
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService,
) {
    @PostMapping
    fun create(@RequestBody request: CreateOrderRequest): ResponseEntity<CreateOrderResponse> {
        val orderId =
            orderService.placeOrder(
                PlaceOrderCommand(
                    userId = request.userId,
                    accountId = request.accountId,
                    amount = request.amount,
                    forceFail = request.forceFail,
                ),
            )

        return ResponseEntity.status(HttpStatus.CREATED).body(CreateOrderResponse(orderId))
    }
}

data class CreateOrderRequest(
    val userId: Long,
    val accountId: Long,
    val amount: Long,
    val forceFail: Boolean = false,
)

data class CreateOrderResponse(
    val orderId: Long,
)
