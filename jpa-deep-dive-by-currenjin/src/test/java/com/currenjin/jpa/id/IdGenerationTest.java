package com.currenjin.jpa.id;

import com.currenjin.support.LogCapture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class IdGenerationTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    void identity를_persist하면_insert가_발생한다() {
        BookIdentity book = new BookIdentity();

        List<String> sqlLogs = LogCapture.execute(() -> entityManager.persist(book));

        List<String> insertQueries = sqlLogs.stream()
                .filter(sql -> sql.trim().toUpperCase().contains("INSERT"))
                .toList();

        assertEquals(1, insertQueries.size());
    }

    @Test
    @Transactional
    void sequence를_persist하면_call이_발생한다() {
        BookSequence book = new BookSequence();

        List<String> sqlLogs = LogCapture.execute(() -> entityManager.persist(book));

        List<String> insertQueries = sqlLogs.stream()
                .filter(sql -> sql.trim().toUpperCase().contains("CALL NEXT VALUE FOR HIBERNATE_SEQUENCE"))
                .toList();

        assertEquals(1, insertQueries.size());
    }

    @Test
    @Transactional
    void auto를_persist하면_call이_발생한다() {
        BookAuto book = new BookAuto();

        List<String> sqlLogs = LogCapture.execute(() -> entityManager.persist(book));

        List<String> insertQueries = sqlLogs.stream()
                .filter(sql -> sql.trim().toUpperCase().contains("CALL NEXT VALUE FOR HIBERNATE_SEQUENCE"))
                .toList();

        assertEquals(1, insertQueries.size());
    }

    @Test
    @Transactional
    void table을_persist하면_select와_update가_발생한다() {
        BookTable book = new BookTable();

        List<String> sqlLogs = LogCapture.execute(() -> entityManager.persist(book));

        List<String> insertQueries = sqlLogs.stream()
                .filter(sql -> sql.trim().toUpperCase().contains("SELECT")
                        || sql.trim().toUpperCase().contains("UPDATE"))
                .toList();

        assertEquals(2, insertQueries.size());
    }

    @Entity
    public class BookIdentity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    }

    @Entity
    public class BookSequence {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;
    }

    @Entity
    public class BookAuto {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    }

    @Entity
    public class BookTable {
        @Id
        @GeneratedValue(strategy = GenerationType.TABLE)
        private Long id;
    }
}
