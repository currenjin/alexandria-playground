package com.currenjin.order.controller;

import com.currenjin.order.dto.OrderRequest;
import com.currenjin.order.dto.OrderResponse;
import com.currenjin.order.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(request);
    }
}
