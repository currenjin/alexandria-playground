package com.currenjin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    private LocalDateTime orderDate;
    private LocalDateTime createdAt;

    @Builder
    public Order(Member member, OrderStatus status, LocalDateTime orderDate) {
        this.member = member;
        this.status = status;
        this.orderDate = orderDate != null ? orderDate : LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }
}
