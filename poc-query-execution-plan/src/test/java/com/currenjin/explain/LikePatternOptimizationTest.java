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
@DisplayName("Scenario 4: LIKE Pattern Optimization")
class LikePatternOptimizationTest {

    @Autowired
    private DataSource dataSource;

    private ExplainAnalyzer explain;

    @BeforeEach
    void setUp() {
        explain = new ExplainAnalyzer(dataSource);

        explain.execute("TRUNCATE TABLE product");

        explain.dropIndexIfExists("idx_product_name", "product");

        DataFactory.insertProducts(dataSource, 10_000);

        explain.createIndex("idx_product_name", "product", "name");
    }

    @Test
    @DisplayName("LIKE '%keyword%' 패턴은 인덱스를 사용할 수 없다 (leading wildcard)")
    void 양쪽_와일드카드_패턴은_인덱스를_사용할수_없다() {
        ExplainResult result = explain.explainFirst(
                "SELECT * FROM product WHERE name LIKE ?", "%Laptop%"
        );

        assertThat(result.getType()).isEqualTo("ALL");

        System.out.println("[LIKE '%keyword%'] type=" + result.getType()
                + ", rows=" + result.getRows()
                + ", key=" + result.getKey());
    }

    @Test
    @DisplayName("LIKE 'keyword%' 패턴은 인덱스를 사용할 수 있다 (prefix search)")
    void 접두사_패턴은_인덱스를_사용할수_있다() {
        ExplainResult result = explain.explainFirst(
                "SELECT * FROM product WHERE name LIKE ?", "Laptop%"
        );

        assertThat(result.getType()).isIn("range", "ref");
        assertThat(result.getKey()).isEqualTo("idx_product_name");

        System.out.println("[LIKE 'keyword%'] type=" + result.getType()
                + ", rows=" + result.getRows()
                + ", key=" + result.getKey());
    }

    @Test
    @DisplayName("BEFORE vs AFTER 비교: 스캔 행 수 차이 확인")
    void 스캔_행수_비교() {
        ExplainResult fullScan = explain.explainFirst(
                "SELECT * FROM product WHERE name LIKE ?", "%Laptop%"
        );
        ExplainResult rangeScan = explain.explainFirst(
                "SELECT * FROM product WHERE name LIKE ?", "Laptop%"
        );

        assertThat(rangeScan.getRows()).isLessThan(fullScan.getRows());

        System.out.println("Full scan rows: " + fullScan.getRows());
        System.out.println("Range scan rows: " + rangeScan.getRows());
        System.out.println("Improvement: " + (fullScan.getRows() - rangeScan.getRows()) + " fewer rows scanned");

        explain.dropIndex("idx_product_name", "product");
    }
}
