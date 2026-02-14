package com.currenjin.seata.order

import com.currenjin.seata.account.AccountService
import io.seata.spring.annotation.GlobalTransactional
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val accountService: AccountService,
    private val orderRepository: OrderRepository,
) {
    @GlobalTransactional(name = "create-order-tx", rollbackFor = [Exception::class])
    fun placeOrder(command: PlaceOrderCommand): Long {
        accountService.decreaseBalance(command.accountId, command.amount)
        val orderId = orderRepository.create(command.userId, command.accountId, command.amount)

        if (command.forceFail) {
            throw IllegalStateException("주문 후 의도적 실패를 발생시켰습니다.")
        }

        return orderId
    }
}

data class PlaceOrderCommand(
    val userId: Long,
    val accountId: Long,
    val amount: Long,
    val forceFail: Boolean,
)
