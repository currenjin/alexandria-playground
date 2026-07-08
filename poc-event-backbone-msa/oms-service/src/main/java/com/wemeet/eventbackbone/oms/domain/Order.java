package com.wemeet.eventbackbone.oms.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 주문 도메인 엔티티. Spring Data JDBC 매핑(@Table/@Column) — save()는 신규 INSERT 전용이라 isNew()=true,
 * 상태 변경은 리포지토리의 명시 update로 처리한다.
 */
@Table("orders")
public record Order(
        @Id @Column("order_id") String orderId,
        @Column("customer_id") String customerId,
        @Column("amount") String amount,
        @Column("currency") String currency,
        @Column("status") String status
) implements Persistable<String> {

    public static final String CONFIRMED = "CONFIRMED";
    public static final String CANCELLED = "CANCELLED";

    public static Order confirm(String orderId, String customerId, String amount, String currency) {
        return new Order(orderId, customerId, amount, currency, CONFIRMED);
    }

    @Override
    public String getId() {
        return orderId;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
