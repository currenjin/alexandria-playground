package com.currenjin.domain.repository;

import com.currenjin.application.dto.OrderSearchDto;
import com.currenjin.application.dto.OrderSimpleQueryDto;
import com.currenjin.domain.Order;
import com.currenjin.domain.Order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findOrdersWithMemberAndDelivery();
    List<OrderSimpleQueryDto> findOrderDtos();
    List<Order> searchOrders(OrderSearchDto orderSearchDto);
    List<Order> findOrdersInPeriod(LocalDateTime fromDate, LocalDateTime toDate);
    List<Order> findOrdersByStatus(OrderStatus status);
}