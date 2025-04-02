package com.currenjin.querydsl;

import com.currenjin.domain.*;
import com.currenjin.domain.Member.MemberStatus;
import com.currenjin.domain.Order.OrderStatus;
import com.currenjin.domain.Product.ProductStatus;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ComplexQuerydslTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void setup() {
        queryFactory = new JPAQueryFactory(em);
        // 이 테스트에서는 QuerydslTest에서 초기화된 데이터를 사용
    }

    @Test
    public void complexJoinAndGroupBy() {
        QMember member = QMember.member;
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        // 회원별로 카테고리 상품의 총 주문 금액을 조회
        List<Tuple> results = queryFactory
                .select(
                        member.name,
                        category.name,
                        orderItem.orderPrice.multiply(orderItem.count).sum()
                )
                .from(orderItem)
                .join(orderItem.order, order)
                .join(order.member, member)
                .join(orderItem.product, product)
                .join(product.category, category)
                .groupBy(member.name, category.name)
                .fetch();

        assertThat(results).isNotEmpty();
    }

    @Test
    public void hierarchicalQuery() {
        QCategory parent = new QCategory("parent");
        QCategory child = new QCategory("child");

        // 하위 카테고리가 있는 카테고리 조회
        List<Category> categories = queryFactory
                .selectFrom(parent)
                .where(
                        JPAExpressions
                                .selectFrom(child)
                                .where(child.parent.eq(parent))
                                .exists()
                )
                .fetch();

        // 상위 카테고리가 없는 최상위 카테고리 조회
        List<Category> rootCategories = queryFactory
                .selectFrom(parent)
                .where(parent.parent.isNull())
                .fetch();

        assertThat(rootCategories).isNotEmpty();
    }

    @Test
    public void complexDynamicQuery() {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        String nameContains = "북";
        Integer minPrice = 10000;
        Integer maxPrice = 1500000;
        String categoryName = "전자";
        Boolean inStock = true;

        List<Product> results = queryFactory
                .selectFrom(product)
                .leftJoin(product.category, category)
                .where(
                        containsName(nameContains),
                        betweenPrice(minPrice, maxPrice),
                        categoryNameContains(categoryName),
                        isInStock(inStock)
                )
                .fetch();

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getName()).contains(nameContains);
        assertThat(results.get(0).getPrice()).isGreaterThanOrEqualTo(minPrice);
        assertThat(results.get(0).getPrice()).isLessThanOrEqualTo(maxPrice);
        assertThat(results.get(0).getCategory().getName()).contains(categoryName);
        assertThat(results.get(0).getStockQuantity()).isGreaterThan(0);
    }

    private BooleanExpression containsName(String nameContains) {
        return nameContains != null ? QProduct.product.name.contains(nameContains) : null;
    }

    private BooleanExpression betweenPrice(Integer minPrice, Integer maxPrice) {
        if (minPrice == null && maxPrice == null) return null;
        if (minPrice == null) return QProduct.product.price.loe(maxPrice);
        if (maxPrice == null) return QProduct.product.price.goe(minPrice);
        return QProduct.product.price.between(minPrice, maxPrice);
    }

    private BooleanExpression categoryNameContains(String categoryName) {
        return categoryName != null ? QCategory.category.name.contains(categoryName) : null;
    }

    private BooleanExpression isInStock(Boolean inStock) {
        if (inStock == null) return null;
        return inStock ? QProduct.product.stockQuantity.gt(0) : QProduct.product.stockQuantity.eq(0);
    }

    @Test
    public void advancedOrderStatistics() {
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        // 카테고리별 주문 통계
        List<Tuple> categoryStats = queryFactory
                .select(
                        category.name,
                        orderItem.count().as("orderCount"),
                        orderItem.orderPrice.multiply(orderItem.count).sum().as("totalAmount")
                )
                .from(orderItem)
                .join(orderItem.product, product)
                .join(product.category, category)
                .groupBy(category.name)
                .orderBy(orderItem.orderPrice.multiply(orderItem.count).sum().desc())
                .fetch();

        assertThat(categoryStats).isNotEmpty();
    }

    @Test
    public void dateRangeQueries() {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        // 특정 기간 내의 주문 조회
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();

        List<Order> periodOrders = queryFactory
                .selectFrom(order)
                .where(
                        order.orderDate.between(startDate, endDate)
                )
                .orderBy(order.orderDate.desc())
                .fetch();

        // 특정 회원의 첫 주문 조회
        Order firstOrder = queryFactory
                .selectFrom(order)
                .join(order.member, member)
                .where(member.name.eq("김철수"))
                .orderBy(order.orderDate.asc())
                .fetchFirst();

        assertThat(firstOrder).isNotNull();
    }

    @Test
    public void fetchJoinOptimization() {
        QOrder order = QOrder.order;
        QMember member = QMember.member;
        QOrderItem orderItem = QOrderItem.orderItem;
        QProduct product = QProduct.product;

        // 주문과 관련 엔티티를 모두 페치 조인으로 최적화하여 조회
        List<Order> orders = queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(orderItem.product, product).fetchJoin()
                .where(order.status.eq(OrderStatus.PENDING))
                .distinct()
                .fetch();

        assertThat(orders).isNotEmpty();
    }

    @Test
    public void customProjectionDto() {
        QMember member = QMember.member;
        QOrder order = QOrder.order;

        // 회원과 주문 정보를 DTO로 프로젝션
        List<MemberOrderSummary> summaries = queryFactory
                .select(Projections.constructor(
                        MemberOrderSummary.class,
                        member.id,
                        member.name,
                        member.email,
                        order.count(),
                        order.orderItems.size().sum()
                ))
                .from(member)
                .leftJoin(member.orders, order)
                .groupBy(member.id, member.name, member.email)
                .fetch();

        assertThat(summaries).isNotEmpty();
    }

    public static class MemberOrderSummary {
        private final Long memberId;
        private final String name;
        private final String email;
        private final Long orderCount;
        private final Long itemCount;

        public MemberOrderSummary(Long memberId, String name, String email, Long orderCount, Long itemCount) {
            this.memberId = memberId;
            this.name = name;
            this.email = email;
            this.orderCount = orderCount;
            this.itemCount = itemCount;
        }

        public Long getMemberId() {
            return memberId;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public Long getOrderCount() {
            return orderCount;
        }

        public Long getItemCount() {
            return itemCount;
        }
    }
}