package com.currenjin.application.dto;

import com.currenjin.domain.Address;
import com.currenjin.domain.Order.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String memberName;
    private OrderStatus orderStatus;
    private Address address;
    private LocalDateTime orderDate;
    private int totalPrice;

    public OrderSimpleQueryDto(Long orderId, String memberName, OrderStatus orderStatus,
                               Address address, LocalDateTime orderDate, int totalPrice) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.orderStatus = orderStatus;
        this.address = address;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }
}
