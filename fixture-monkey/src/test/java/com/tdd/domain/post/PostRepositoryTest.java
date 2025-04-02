package com.tdd.domain.post;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.tdd.domain.comment.Comment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class PostRepositoryTest {
    FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
            .register(Post.class, arbitraryBuilder ->
                    arbitraryBuilder.giveMeBuilder(Post.class)
                            .minSize("id", 0)
                            .set("comments", new ArrayList<>()))
            .build();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PostRepository repository;

    private Post post;

    private Comment comment;

    @BeforeEach
    void setUp() {
        post = fixtureMonkey.giveMeOne(Post.class);
    }

    @AfterEach
    void tearDown() {
        em.clear();
    }

    @Test
    void creation() {
        Query query = em.createQuery("select count(p) from Post p");
        Long before = (Long) query.getSingleResult();

        repository.save(post);

        assertEquals(before + 1L, query.getSingleResult());
    }

    @Test
    void creation_with_comment() {
        Query query = em.createQuery("select count(p) from Post p");
        Long before = (Long) query.getSingleResult();

        comment = fixtureMonkey.giveMeOne(Comment.class);
        post.addComment(comment);

        repository.save(post);

        assertEquals(before + 1L, query.getSingleResult());
    }

    @Test
    void creation_with_many_comment() {
        Query query = em.createQuery("select count(p) from Post p");
        Long before = (Long) query.getSingleResult();

        List<Comment> comments = fixtureMonkey.giveMe(Comment.class, 5);
        comments.forEach(comment -> post.addComment(comment));

        repository.save(post);

        assertEquals(before + 1L, query.getSingleResult());
    }

    @Test
    void many_creation() {
        Query query = em.createQuery("select count(p) from Post p");
        Long before = (Long) query.getSingleResult();
        List<Post> posts = fixtureMonkey.giveMe(Post.class, 5);

        repository.saveAll(posts);

        assertEquals(before + 5L, query.getSingleResult());
    }
}