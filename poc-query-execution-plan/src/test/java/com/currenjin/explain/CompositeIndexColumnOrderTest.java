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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Scenario 2: Composite Index Column Order")
class CompositeIndexColumnOrderTest {

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

        explain.dropIndexIfExists("idx_orders_wrong", "orders");
        explain.dropIndexIfExists("idx_orders_correct", "orders");

        DataFactory.insertMembers(dataSource, 1_000);
        DataFactory.insertOrders(dataSource, 20_000, 1_000);
    }

    @Test
    @DisplayName("인덱스 없이 status + order_date 범위 검색 시 Full Table Scan")
    void 인덱스_없이_복합조건_검색시_풀테이블스캔() {
        ExplainResult result = explain.explainFirst(
                "SELECT * FROM orders WHERE status = ? AND order_date > ?",
                "PENDING", "2025-06-01"
        );

        assertThat(result.getType()).isEqualTo("ALL");

        System.out.println("[NO INDEX] type=" + result.getType()
                + ", rows=" + result.getRows());
    }

    @Test
    @DisplayName("잘못된 순서의 복합 인덱스 (order_date, status) vs 올바른 순서 (status, order_date)")
    void 복합인덱스_컬럼순서_비교() {
        // 잘못된 순서: 범위 조건 컬럼이 먼저
        explain.createIndex("idx_orders_wrong", "orders", "order_date", "status");

        ExplainResult wrongResult = explain.explainFirst(
                "SELECT * FROM orders WHERE status = ? AND order_date > ?",
                "PENDING", "2025-06-01"
        );

        System.out.println("[WRONG ORDER] type=" + wrongResult.getType()
                + ", rows=" + wrongResult.getRows()
                + ", key=" + wrongResult.getKey());

        Long wrongOrderRows = wrongResult.getRows();

        explain.dropIndex("idx_orders_wrong", "orders");

        // 올바른 순서: 등치 조건 컬럼이 먼저
        explain.createIndex("idx_orders_correct", "orders", "status", "order_date");

        ExplainResult correctResult = explain.explainFirst(
                "SELECT * FROM orders WHERE status = ? AND order_date > ?",
                "PENDING", "2025-06-01"
        );

        assertThat(correctResult.getType()).isIn("range", "ref");
        assertThat(correctResult.getKey()).isEqualTo("idx_orders_correct");

        System.out.println("[CORRECT ORDER] type=" + correctResult.getType()
                + ", rows=" + correctResult.getRows()
                + ", key=" + correctResult.getKey());

        assertThat(correctResult.getRows()).isLessThanOrEqualTo(wrongOrderRows);

        explain.dropIndex("idx_orders_correct", "orders");
    }
}
