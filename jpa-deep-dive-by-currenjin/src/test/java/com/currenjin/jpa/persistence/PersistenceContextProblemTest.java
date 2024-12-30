package com.currenjin.jpa.persistence;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.currenjin.domain.Comment;
import com.currenjin.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PersistenceContextProblemTest {
    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    void setUp() {
        Post post1 = new Post();
        entityManager.persist(post1);

        Comment comment1 = new Comment();
        comment1.setPost(post1);
        entityManager.persist(comment1);

        Comment comment2 = new Comment();
        comment2.setPost(post1);
        entityManager.persist(comment2);

        post1.getComments().add(comment1);
        post1.getComments().add(comment2);

        Post post2 = new Post();
        entityManager.persist(post2);

        Comment comment3 = new Comment();
        comment3.setPost(post2);
        entityManager.persist(comment3);

        Comment comment4 = new Comment();
        comment4.setPost(post2);
        entityManager.persist(comment4);

        post2.getComments().add(comment3);
        post2.getComments().add(comment4);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Transactional
    void N플러스1_문제_발생_테스트() {
        List<String> sqlLogs = new ArrayList<>();

        List<Post> posts = entityManager.createQuery("select p from Post p", Post.class)
                .getResultList();

        logCapture(() -> {
            for (Post post : posts) {
                post.getComments().size();
            }
        }, sqlLogs);

        List<String> selectQueries = getSelectQueries(sqlLogs);

        assertEquals(2, selectQueries.size());
    }


    @Test
    @Transactional
    void 페치_조인으로_해결() {
        List<String> sqlLogs = new ArrayList<>();

        List<Post> posts = entityManager
                .createQuery("select distinct p from Post p join fetch p.comments", Post.class)
                .getResultList();

        logCapture(() -> {
            for (Post post : posts) {
                post.getComments().size();
            }
        }, sqlLogs);

        List<String> selectQueries = getSelectQueries(sqlLogs);

        assertEquals(0, selectQueries.size());
    }

    @Test
    @Transactional
    void EntityGraph로_해결() {
        List<String> sqlLogs = new ArrayList<>();

        List<Post> posts = entityManager.createQuery("select p from Post p", Post.class)
                .setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("Post.withComments"))
                .getResultList();

        logCapture(() -> {
            for (Post post : posts) {
                post.getComments().size();
            }
        }, sqlLogs);

        List<String> selectQueries = getSelectQueries(sqlLogs);

        assertEquals(0, selectQueries.size());
    }

    private List<String> getSelectQueries(List<String> sqlLogs) {
        return sqlLogs.stream()
                .filter(sql -> sql.trim().toUpperCase().startsWith("SELECT"))
                .toList();
    }

    private void logCapture(Runnable runnable, List<String> sqlLogs) {
        Logger logger = (Logger) LoggerFactory.getLogger("org.hibernate.SQL");
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        try {
            runnable.run();
            sqlLogs.addAll(listAppender.list.stream()
                    .map(ILoggingEvent::getMessage)
                    .toList());
        } finally {
            logger.detachAppender(listAppender);
        }
    }
}
