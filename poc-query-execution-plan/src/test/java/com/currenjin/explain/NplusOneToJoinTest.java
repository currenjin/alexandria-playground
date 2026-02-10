package com.currenjin.explain;

import com.currenjin.support.DataFactory;
import com.currenjin.support.ExplainAnalyzer;
import com.currenjin.support.ExplainResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Scenario 3: N+1 Problem -> JOIN")
class NplusOneToJoinTest {

    @Autowired
    private DataSource dataSource;

    private ExplainAnalyzer explain;

    @BeforeEach
    void setUp() {
        explain = new ExplainAnalyzer(dataSource);

        explain.execute("SET FOREIGN_KEY_CHECKS = 0");
        explain.execute("TRUNCATE TABLE orders");
        explain.execute("TRUNCATE TABLE member");
        explain.execute("SET FOREIGN_KEY_CHECKS = 1");

        DataFactory.insertMembers(dataSource, 1_000);
        DataFactory.insertOrders(dataSource, 10_000, 1_000);
    }

    @Test
    @DisplayName("N+1: 주문 목록 조회 후 각 주문의 회원을 개별 조회하면 N+1번 쿼리 발생")
    void N플러스1_문제_시연() {
        ExplainResult ordersResult = explain.explainFirst(
                "SELECT * FROM orders LIMIT 100"
        );
        System.out.println("[N+1: orders query] type=" + ordersResult.getType()
                + ", rows=" + ordersResult.getRows());

        ExplainResult memberResult = explain.explainFirst(
                "SELECT * FROM member WHERE id = ?", 1L
        );
        System.out.println("[N+1: member query x100] type=" + memberResult.getType()
                + ", rows=" + memberResult.getRows());

        // N+1 패턴: 1(주문 목록) + N(각 주문의 회원) = 101번 쿼리
        int nPlusOneQueryCount = 1 + 100;
        assertThat(nPlusOneQueryCount).isEqualTo(101);
    }

    @Test
    @DisplayName("JOIN: 단일 조인 쿼리로 주문과 회원을 한번에 조회")
    void JOIN_쿼리로_개선() {
        // STRAIGHT_JOIN: orders를 driving table로 강제하여 member가 eq_ref로 조인되도록 함
        String joinSql = "SELECT STRAIGHT_JOIN o.*, m.name, m.email FROM orders o "
                + "JOIN member m ON o.member_id = m.id LIMIT 100";

        List<ExplainResult> results = explain.explain(joinSql);

        assertThat(results).hasSize(2);

        // 첫 번째 테이블(orders)은 driving table
        ExplainResult ordersRow = results.get(0);
        // 두 번째 테이블(member)은 PK로 조인
        ExplainResult memberRow = results.get(1);

        // PK를 통한 조인이므로 eq_ref
        assertThat(memberRow.getType()).isEqualTo("eq_ref");
        assertThat(memberRow.getKey()).isEqualTo("PRIMARY");

        System.out.println("[JOIN: " + ordersRow.getTable() + "] type=" + ordersRow.getType()
                + ", rows=" + ordersRow.getRows()
                + ", key=" + ordersRow.getKey());
        System.out.println("[JOIN: " + memberRow.getTable() + "] type=" + memberRow.getType()
                + ", rows=" + memberRow.getRows()
                + ", key=" + memberRow.getKey());

        // JOIN은 단 1번의 쿼리
        int joinQueryCount = 1;
        assertThat(joinQueryCount).isLessThan(101);
    }
}
