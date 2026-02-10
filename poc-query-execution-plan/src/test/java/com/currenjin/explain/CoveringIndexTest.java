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
@DisplayName("Scenario 6: Covering Index")
class CoveringIndexTest {

    @Autowired
    private DataSource dataSource;

    private ExplainAnalyzer explain;

    @BeforeEach
    void setUp() {
        explain = new ExplainAnalyzer(dataSource);

        explain.execute("TRUNCATE TABLE product");

        explain.dropIndexIfExists("idx_product_status", "product");
        explain.dropIndexIfExists("idx_product_covering", "product");

        DataFactory.insertProducts(dataSource, 10_000);
    }

    @Test
    @DisplayName("단일 컬럼 인덱스로 조회 시 테이블 룩업 필요")
    void 단일_컬럼_인덱스는_테이블_룩업이_필요하다() {
        explain.createIndex("idx_product_status", "product", "status");

        ExplainResult result = explain.explainFirst(
                "SELECT name, price, status FROM product WHERE status = ?", "AVAILABLE"
        );

        assertThat(result.getKey()).isEqualTo("idx_product_status");
        assertThat(result.usesIndex()).isFalse();

        System.out.println("[SINGLE INDEX] type=" + result.getType()
                + ", key=" + result.getKey()
                + ", Extra=" + result.getExtra());

        explain.dropIndex("idx_product_status", "product");
    }

    @Test
    @DisplayName("커버링 인덱스로 조회 시 테이블 룩업 불필요 (Using index)")
    void 커버링_인덱스는_테이블_룩업이_불필요하다() {
        explain.createIndex("idx_product_covering", "product", "status", "name", "price");

        ExplainResult result = explain.explainFirst(
                "SELECT name, price, status FROM product WHERE status = ?", "AVAILABLE"
        );

        assertThat(result.getKey()).isEqualTo("idx_product_covering");
        assertThat(result.usesIndex()).isTrue();

        System.out.println("[COVERING INDEX] type=" + result.getType()
                + ", key=" + result.getKey()
                + ", Extra=" + result.getExtra());

        explain.dropIndex("idx_product_covering", "product");
    }

    @Test
    @DisplayName("커버링 인덱스 vs 일반 인덱스: Extra 컬럼 비교")
    void 커버링_인덱스와_일반_인덱스_Extra_비교() {
        explain.createIndex("idx_product_status", "product", "status");

        ExplainResult withoutCovering = explain.explainFirst(
                "SELECT name, price, status FROM product WHERE status = ?", "AVAILABLE"
        );

        explain.dropIndex("idx_product_status", "product");
        explain.createIndex("idx_product_covering", "product", "status", "name", "price");

        ExplainResult withCovering = explain.explainFirst(
                "SELECT name, price, status FROM product WHERE status = ?", "AVAILABLE"
        );

        System.out.println("[WITHOUT COVERING] Extra=" + withoutCovering.getExtra());
        System.out.println("[WITH COVERING]    Extra=" + withCovering.getExtra());

        assertThat(withCovering.usesIndex()).isTrue();
        assertThat(withoutCovering.usesIndex()).isFalse();

        explain.dropIndex("idx_product_covering", "product");
    }
}
