package com.currenjin.querydsl;

import com.currenjin.domain.*;
import com.currenjin.domain.Member.MemberStatus;
import com.currenjin.domain.Order.OrderStatus;
import com.currenjin.domain.Product.ProductStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void setup() {
        queryFactory = new JPAQueryFactory(em);

        // 테스트 데이터 초기화
        initTestData();
    }

    private void initTestData() {
        // 카테고리 데이터 생성
        Category electronics = Category.builder()
                .name("전자제품")
                .build();

        Category clothing = Category.builder()
                .name("의류")
                .build();

        Category food = Category.builder()
                .name("식품")
                .build();

        em.persist(electronics);
        em.persist(clothing);
        em.persist(food);

        // 상품 데이터 생성
        Product laptop = Product.builder()
                .name("노트북")
                .price(1000000)
                .stockQuantity(10)
                .status(ProductStatus.AVAILABLE)
                .category(electronics)
                .build();

        Product phone = Product.builder()
                .name("스마트폰")
                .price(800000)
                .stockQuantity(20)
                .status(ProductStatus.AVAILABLE)
                .category(electronics)
                .build();

        Product shirt = Product.builder()
                .name("셔츠")
                .price(50000)
                .stockQuantity(100)
                .status(ProductStatus.AVAILABLE)
                .category(clothing)
                .build();

        Product apple = Product.builder()
                .name("사과")
                .price(3000)
                .stockQuantity(500)
                .status(ProductStatus.AVAILABLE)
                .category(food)
                .build();

        em.persist(laptop);
        em.persist(phone);
        em.persist(shirt);
        em.persist(apple);

        // 회원 데이터 생성
        Member member1 = Member.builder()
                .name("김철수")
                .email("kim@email.com")
                .age(30)
                .status(MemberStatus.ACTIVE)
                .address(new Address("서울", "강남구", "12345"))
                .build();

        Member member2 = Member.builder()
                .name("이영희")
                .email("lee@email.com")
                .age(25)
                .status(MemberStatus.ACTIVE)
                .address(new Address("서울", "마포구", "54321"))
                .build();

        Member member3 = Member.builder()
                .name("박민수")
                .email("park@email.com")
                .age(40)
                .status(MemberStatus.INACTIVE)
                .address(new Address("부산", "해운대구", "98765"))
                .build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        // 주문 데이터 생성
        Order order1 = Order.builder()
                .member(member1)
                .status(OrderStatus.DELIVERED)
                .deliveryAddress(member1.getAddress())
                .build();

        OrderItem orderItem1 = OrderItem.builder()
                .product(laptop)
                .orderPrice(laptop.getPrice())
                .count(1)
                .build();

        order1.addOrderItem(orderItem1);

        Order order2 = Order.builder()
                .member(member2)
                .status(OrderStatus.PENDING)
                .deliveryAddress(member2.getAddress())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .product(phone)
                .orderPrice(phone.getPrice())
                .count(1)
                .build();

        OrderItem orderItem3 = OrderItem.builder()
                .product(shirt)
                .orderPrice(shirt.getPrice())
                .count(2)
                .build();

        order2.addOrderItem(orderItem2);
        order2.addOrderItem(orderItem3);

        em.persist(order1);
        em.persist(order2);
    }

    @Test
    public void basicQuery() {
        QMember member = QMember.member;

        Member foundMember = queryFactory
                .selectFrom(member)
                .where(member.name.eq("김철수"))
                .fetchOne();

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getName()).isEqualTo("김철수");
    }

    @Test
    public void joinTest() {
        QOrder order = QOrder.order;
        QMember member = QMember.member;
        QOrderItem orderItem = QOrderItem.orderItem;
        QProduct product = QProduct.product;

        List<Order> orders = queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.product, product).fetchJoin()
                .where(member.name.eq("이영희"))
                .fetch();

        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getMember().getName()).isEqualTo("이영희");
    }

    @Test
    public void aggregationTest() {
        QProduct product = QProduct.product;

        Tuple result = queryFactory
                .select(
                        product.price.avg(),
                        product.price.sum(),
                        product.price.min(),
                        product.price.max(),
                        product.count()
                )
                .from(product)
                .fetchOne();

        assertThat(result).isNotNull();
        assertThat(result.get(product.count())).isEqualTo(4);
    }

    @Test
    public void groupByTest() {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        List<Tuple> results = queryFactory
                .select(category.name, product.price.avg())
                .from(product)
                .join(product.category, category)
                .groupBy(category.name)
                .fetch();

        assertThat(results).hasSize(3);
    }

    @Test
    public void caseTest() {
        QProduct product = QProduct.product;

        List<Tuple> results = queryFactory
                .select(
                        product.name,
                        new CaseBuilder()
                                .when(product.price.lt(10000)).then("저가")
                                .when(product.price.lt(100000)).then("중가")
                                .otherwise("고가")
                )
                .from(product)
                .fetch();

        assertThat(results).hasSize(4);
    }

    @Test
    public void projectionTest() {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        // POJO 객체로 프로젝션
        List<ProductProjection> results = queryFactory
                .select(Projections.constructor(
                        ProductProjection.class,
                        product.id,
                        product.name,
                        product.price,
                        category.name
                ))
                .from(product)
                .join(product.category, category)
                .fetch();

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getProductName()).isNotNull();
    }

    @Test
    public void dynamicQueryTest() {
        QProduct product = QProduct.product;

        String nameParam = "노트북";
        Integer priceParam = null; // 가격 조건은 없음

        BooleanBuilder builder = new BooleanBuilder();

        if (nameParam != null) {
            builder.and(product.name.contains(nameParam));
        }

        if (priceParam != null) {
            builder.and(product.price.gt(priceParam));
        }

        List<Product> results = queryFactory
                .selectFrom(product)
                .where(builder)
                .fetch();

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getName()).contains("노트북");
    }

    @Test
    public void dynamicQueryTest2() {
        QProduct product = QProduct.product;

        String nameParam = "노트북";
        Integer priceParam = null; // 가격 조건은 없음

        List<Product> results = queryFactory
                .selectFrom(product)
                .where(
                        nameContains(nameParam),
                        priceGt(priceParam)
                )
                .fetch();

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getName()).contains("노트북");
    }

    private BooleanExpression nameContains(String nameParam) {
        return nameParam != null ? QProduct.product.name.contains(nameParam) : null;
    }

    private BooleanExpression priceGt(Integer priceParam) {
        return priceParam != null ? QProduct.product.price.gt(priceParam) : null;
    }

    @Test
    public void subQueryTest() {
        QProduct product = QProduct.product;
        QProduct subProduct = new QProduct("subProduct");

        List<Product> results = queryFactory
                .selectFrom(product)
                .where(product.price.gt(
                        JPAExpressions
                                .select(subProduct.price.avg())
                                .from(subProduct)
                ))
                .fetch();

        assertThat(results).isNotEmpty();
    }

    // DTO 클래스
    public static class ProductProjection {
        private Long productId;
        private String productName;
        private int price;
        private String categoryName;

        public ProductProjection(Long productId, String productName, int price, String categoryName) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.categoryName = categoryName;
        }

        public Long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public int getPrice() {
            return price;
        }

        public String getCategoryName() {
            return categoryName;
        }
    }
}