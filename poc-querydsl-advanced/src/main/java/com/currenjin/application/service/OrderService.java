package com.currenjin.application.service;

import com.currenjin.application.dto.OrderSearchDto;
import com.currenjin.application.dto.OrderSimpleQueryDto;
import com.currenjin.domain.Member;
import com.currenjin.domain.Order;
import com.currenjin.domain.Order.OrderStatus;
import com.currenjin.domain.OrderItem;
import com.currenjin.domain.Product;
import com.currenjin.domain.repository.MemberRepository;
import com.currenjin.domain.repository.OrderRepository;
import com.currenjin.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long order(Long memberId, Long productId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // 주문상품 생성
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .orderPrice(product.getPrice())
                .count(count)
                .build();

        // 주문 생성
        Order order = Order.builder()
                .member(member)
                .status(OrderStatus.PENDING)
                .deliveryAddress(member.getAddress())
                .build();

        // 주문상품과 주문 연결
        order.addOrderItem(orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 주문 취소
        order.getOrderItems().forEach(OrderItem::cancel);
        order.setStatus(OrderStatus.CANCELLED);
    }

    public List<Order> findOrders() {
        return orderRepository.findAll();
    }

    public Order findOne(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public List<Order> findOrdersWithMemberAndDelivery() {
        return orderRepository.findOrdersWithMemberAndDelivery();
    }

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return orderRepository.findOrderDtos();
    }

    public List<Order> searchOrders(OrderSearchDto orderSearchDto) {
        return orderRepository.searchOrders(orderSearchDto);
    }

    public List<Order> findOrdersInPeriod(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderRepository.findOrdersInPeriod(fromDate, toDate);
    }

    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findOrdersByStatus(status);
    }
}