package com.currenjin.jpa.dirtycheck;

import com.currenjin.domain.Post;
import com.currenjin.infrastucture.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class DirtyCheckingBasicTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    private Long testPostId;

    @BeforeEach
    public void setUp() {
        postRepository.deleteAll();

        Post post = new Post();
        post.setTitle("원본 제목");
        Post savedPost = postRepository.save(post);
        testPostId = savedPost.getId();
    }

    @Test
    @Transactional
    void 기본_더티체킹_테스트() {
        Post post = entityManager.find(Post.class, testPostId);
        String originalTitle = post.getTitle();

        String newTitle = "변경된 제목";
        post.setTitle(newTitle);

        entityManager.flush();
        entityManager.clear();

        Post updatedPost = entityManager.find(Post.class, testPostId);

        assertEquals(newTitle, updatedPost.getTitle());
        assertNotEquals(originalTitle, updatedPost.getTitle());
    }
}