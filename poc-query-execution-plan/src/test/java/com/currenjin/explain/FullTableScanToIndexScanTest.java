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
@DisplayName("Scenario 1: Full Table Scan -> Index Scan")
class FullTableScanToIndexScanTest {

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

        explain.dropIndexIfExists("idx_member_email", "member");

        DataFactory.insertMembers(dataSource, 10_000);
    }

    @Test
    @DisplayName("인덱스 없이 email 검색 시 Full Table Scan(ALL) 발생")
    void 인덱스_없이_email_검색시_풀테이블스캔() {
        ExplainResult result = explain.explainFirst(
                "SELECT * FROM member WHERE email = ?", "user5000@example.com"
        );

        assertThat(result.getType()).isEqualTo("ALL");
        assertThat(result.getKey()).isNull();
        assertThat(result.getRows()).isGreaterThan(5000L);

        System.out.println("[BEFORE] type=" + result.getType()
                + ", rows=" + result.getRows()
                + ", key=" + result.getKey());
    }

    @Test
    @DisplayName("email 인덱스 생성 후 ref 타입으로 개선")
    void email_인덱스_생성후_ref_타입으로_개선() {
        explain.createIndex("idx_member_email", "member", "email");

        ExplainResult result = explain.explainFirst(
                "SELECT * FROM member WHERE email = ?", "user5000@example.com"
        );

        assertThat(result.getType()).isIn("ref", "const");
        assertThat(result.getKey()).isEqualTo("idx_member_email");
        assertThat(result.getRows()).isLessThanOrEqualTo(10L);

        System.out.println("[AFTER] type=" + result.getType()
                + ", rows=" + result.getRows()
                + ", key=" + result.getKey());

        explain.dropIndex("idx_member_email", "member");
    }
}
