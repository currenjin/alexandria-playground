package com.currenjin.application.dto;

import com.currenjin.domain.Order.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderSearchDto {
    private String memberName;
    private OrderStatus orderStatus;
    private LocalDateTime orderDateFrom;
    private LocalDateTime orderDateTo;
    private String deliveryCity;
}