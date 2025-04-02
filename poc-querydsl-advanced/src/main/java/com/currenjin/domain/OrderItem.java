package com.currenjin.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int orderPrice; // 주문 당시 가격
    private int count; // 주문 수량

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public OrderItem(Product product, int orderPrice, int count) {
        this.product = product;
        this.orderPrice = orderPrice;
        this.count = count;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (product != null) {
            product.getOrderItems().add(this);
            product.removeStock(count);
        }
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }

    public void cancel() {
        if (product != null) {
            product.addStock(count);
        }
    }
}