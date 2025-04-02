package com.currenjin.domain.repository;

import com.currenjin.application.dto.OrderSearchDto;
import com.currenjin.application.dto.OrderSimpleQueryDto;
import com.currenjin.domain.Order;
import com.currenjin.domain.Order.OrderStatus;
import com.currenjin.domain.QMember;
import com.currenjin.domain.QOrder;
import com.currenjin.domain.QOrderItem;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Order> findOrdersWithMemberAndDelivery() {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .fetch();
    }

    @Override
    public List<OrderSimpleQueryDto> findOrderDtos() {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return queryFactory
                .select(Projections.constructor(OrderSimpleQueryDto.class,
                        order.id,
                        member.name,
                        order.status,
                        order.deliveryAddress,
                        order.orderDate,
                        order.orderItems.size()))
                .from(order)
                .join(order.member, member)
                .fetch();
    }

    @Override
    public List<Order> searchOrders(OrderSearchDto orderSearchDto) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(order)
                .join(order.member, member)
                .where(
                        memberNameEq(orderSearchDto.getMemberName()),
                        orderStatusEq(orderSearchDto.getOrderStatus()),
                        orderDateBetween(orderSearchDto.getOrderDateFrom(), orderSearchDto.getOrderDateTo()),
                        deliveryCityEq(orderSearchDto.getDeliveryCity())
                )
                .fetch();
    }

    @Override
    public List<Order> findOrdersInPeriod(LocalDateTime fromDate, LocalDateTime toDate) {
        QOrder order = QOrder.order;

        return queryFactory
                .selectFrom(order)
                .where(orderDateBetween(fromDate, toDate))
                .fetch();
    }

    @Override
    public List<Order> findOrdersByStatus(OrderStatus status) {
        QOrder order = QOrder.order;

        return queryFactory
                .selectFrom(order)
                .where(orderStatusEq(status))
                .fetch();
    }

    // 동적 쿼리를 위한 메서드들
    private BooleanExpression memberNameEq(String memberName) {
        if (memberName == null || memberName.isEmpty()) {
            return null;
        }
        return QMember.member.name.eq(memberName);
    }

    private BooleanExpression orderStatusEq(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return QOrder.order.status.eq(orderStatus);
    }

    private BooleanExpression orderDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null && toDate == null) {
            return null;
        }

        if (fromDate == null) {
            return QOrder.order.orderDate.loe(toDate);
        }

        if (toDate == null) {
            return QOrder.order.orderDate.goe(fromDate);
        }

        return QOrder.order.orderDate.between(fromDate, toDate);
    }

    private BooleanExpression deliveryCityEq(String city) {
        if (city == null || city.isEmpty()) {
            return null;
        }
        return QOrder.order.deliveryAddress.city.eq(city);
    }
}