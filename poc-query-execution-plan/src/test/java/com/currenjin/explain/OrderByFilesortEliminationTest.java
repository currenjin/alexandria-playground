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
@DisplayName("Scenario 5: ORDER BY + LIMIT (filesort elimination)")
class OrderByFilesortEliminationTest {

    @Autowired
    private DataSource dataSource;

    private ExplainAnalyzer explain;

    @BeforeEach
    void setUp() {
        explain = new ExplainAnalyzer(dataSource);

        explain.execute("TRUNCATE TABLE product");

        explain.dropIndexIfExists("idx_product_created_at", "product");

        DataFactory.insertProducts(dataSource, 20_000);

        explain.execute("ANALYZE TABLE product");
    }

    @Test
    @DisplayName("인덱스 없이 ORDER BY created_at DESC LIMIT 10 시 filesort 발생")
    void 인덱스_없이_정렬시_filesort_발생() {
        ExplainResult result = explain.explainFirst(
                "SELECT id, name, created_at FROM product ORDER BY created_at DESC LIMIT 10"
        );

        assertThat(result.usesFilesort()).isTrue();
        assertThat(result.getType()).isEqualTo("ALL");

        System.out.println("[BEFORE] type=" + result.getType()
                + ", rows=" + result.getRows()
                + ", Extra=" + result.getExtra());
    }

    @Test
    @DisplayName("created_at 인덱스 생성 후 filesort 제거")
    void created_at_인덱스_생성후_filesort_제거() {
        explain.createIndex("idx_product_created_at", "product", "created_at");

        ExplainResult result = explain.explainFirst(
                "SELECT id, name, created_at FROM product ORDER BY created_at DESC LIMIT 10"
        );

        assertThat(result.usesFilesort()).isFalse();
        assertThat(result.getKey()).isEqualTo("idx_product_created_at");

        System.out.println("[AFTER] type=" + result.getType()
                + ", rows=" + result.getRows()
                + ", Extra=" + result.getExtra()
                + ", key=" + result.getKey());

        explain.dropIndex("idx_product_created_at", "product");
    }
}
