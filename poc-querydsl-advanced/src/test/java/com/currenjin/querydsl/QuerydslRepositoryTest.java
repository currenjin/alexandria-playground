package com.currenjin.querydsl;

import com.currenjin.application.dto.OrderSearchDto;
import com.currenjin.application.dto.ProductSearchDto;
import com.currenjin.domain.Member;
import com.currenjin.domain.Member.MemberStatus;
import com.currenjin.domain.Order;
import com.currenjin.domain.Order.OrderStatus;
import com.currenjin.domain.Product;
import com.currenjin.domain.Product.ProductStatus;
import com.currenjin.domain.repository.MemberRepository;
import com.currenjin.domain.repository.OrderRepository;
import com.currenjin.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void memberRepositoryTest() {
        // given (QuerydslTest의 초기화 데이터 활용)

        // when
        List<Member> activeMembers = memberRepository.findByStatus(MemberStatus.ACTIVE);
        List<Member> seoulMembers = memberRepository.findByCity("서울");
        List<Member> searchMembers = memberRepository.searchMembers("김", 20, 40, MemberStatus.ACTIVE);

        // then
        assertThat(activeMembers).isNotEmpty();
        assertThat(seoulMembers).isNotEmpty();
        assertThat(searchMembers).isNotEmpty();
    }

    @Test
    public void orderRepositoryTest() {
        // given
        OrderSearchDto searchDto = OrderSearchDto.builder()
                .memberName("김철수")
                .orderStatus(OrderStatus.DELIVERED)
                .build();

        // when
        List<Order> orders = orderRepository.findOrdersWithMemberAndDelivery();
        List<Order> searchOrders = orderRepository.searchOrders(searchDto);
        List<Order> periodOrders = orderRepository.findOrdersInPeriod(
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now());
        List<Order> pendingOrders = orderRepository.findOrdersByStatus(OrderStatus.PENDING);

        // then
        assertThat(orders).isNotEmpty();
        assertThat(searchOrders).isNotEmpty();
        assertThat(periodOrders).isNotEmpty();
        assertThat(pendingOrders).isNotEmpty();
    }

    @Test
    public void productRepositoryTest() {
        // given
        ProductSearchDto searchDto = ProductSearchDto.builder()
                .name("노트북")
                .minPrice(500000)
                .maxPrice(2000000)
                .status(ProductStatus.AVAILABLE)
                .categoryName("전자")
                .inStock(true)
                .build();

        // when
        List<Product> categoryProducts = productRepository.findByCategoryName("전자제품");
        List<Product> priceRangeProducts = productRepository.findByPriceRange(50000, 1000000);
        List<Product> nameAndStatusProducts = productRepository
                .findByNameContainingAndStatus("노트북", ProductStatus.AVAILABLE);
        List<Product> searchProducts = productRepository.searchProducts(searchDto);

        // then
        assertThat(categoryProducts).isNotEmpty();
        assertThat(priceRangeProducts).isNotEmpty();
        assertThat(nameAndStatusProducts).isNotEmpty();
        assertThat(searchProducts).isNotEmpty();
    }
}