package com.currenjin.presentation;

import com.currenjin.application.dto.OrderSearchDto;
import com.currenjin.application.dto.OrderSimpleQueryDto;
import com.currenjin.application.service.OrderService;
import com.currenjin.domain.Order;
import com.currenjin.domain.Order.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/orders")
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        Long orderId = orderService.order(request.getMemberId(), request.getProductId(), request.getCount());
        return new CreateOrderResponse(orderId);
    }

    @PostMapping("/api/orders/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }

    @GetMapping("/api/orders")
    public List<OrderDto> getOrders() {
        List<Order> orders = orderService.findOrders();
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/orders/with-member-delivery")
    public List<OrderDto> getOrdersWithMemberDelivery() {
        List<Order> orders = orderService.findOrdersWithMemberAndDelivery();
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/orders/dto")
    public List<OrderSimpleQueryDto> getOrderDtos() {
        return orderService.findOrderDtos();
    }

    @GetMapping("/api/orders/search")
    public List<OrderDto> searchOrders(@RequestBody OrderSearchDto orderSearchDto) {
        List<Order> orders = orderService.searchOrders(orderSearchDto);
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/orders/period")
    public List<OrderDto> getOrdersInPeriod(
            @RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {
        List<Order> orders = orderService.findOrdersInPeriod(fromDate, toDate);
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/orders/status/{status}")
    public List<OrderDto> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<Order> orders = orderService.findOrdersByStatus(status);
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @Data
    static class CreateOrderRequest {
        private Long memberId;
        private Long productId;
        private int count;
    }

    @Data
    static class CreateOrderResponse {
        private Long orderId;

        public CreateOrderResponse(Long orderId) {
            this.orderId = orderId;
        }
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String memberName;
        private OrderStatus status;
        private LocalDateTime orderDate;
        private int totalPrice;
        private String deliveryCity;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.memberName = order.getMember() != null ? order.getMember().getName() : null;
            this.status = order.getStatus();
            this.orderDate = order.getOrderDate();
            this.totalPrice = order.getTotalPrice();
            this.deliveryCity = order.getDeliveryAddress() != null ? order.getDeliveryAddress().getCity() : null;
        }
    }
}