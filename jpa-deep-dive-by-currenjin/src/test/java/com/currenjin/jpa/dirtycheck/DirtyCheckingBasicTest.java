package com.currenjin.jpa.dirtycheck;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.currenjin.application.DirtyCheckingService;
import com.currenjin.domain.Post;
import com.currenjin.infrastucture.PostRepository;

@SpringBootTest
public class DirtyCheckingBasicTest {
    public static final String NEW_TITLE = "변경된 제목";
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private DirtyCheckingService sut;

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
    void 더티체킹_없이_수정한다() {
        sut.updateTitleWithSave(testPostId, NEW_TITLE);

        Post actual = postRepository.findById(testPostId).get();

        assertEquals(NEW_TITLE, actual.getTitle());
    }

    @Test
    void 더티체킹으로_수정한다() {
        sut.updateTitleWithoutSave(testPostId, NEW_TITLE);

        Post actual = postRepository.findById(testPostId).get();

        assertEquals(NEW_TITLE, actual.getTitle());
    }
}