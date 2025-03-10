package com.currenjin.jpa.dirtycheck;

import com.currenjin.application.DirtyCheckingService;
import com.currenjin.domain.Post;
import com.currenjin.domain.PostWithTransient;
import com.currenjin.infrastucture.PostRepository;
import com.currenjin.infrastucture.PostWithTransientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class DirtyCheckingBasicTest {
    public static final String OLD_TITLE = "원본 제목";
    public static final String NEW_TITLE = "변경된 제목";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostWithTransientRepository postWithTransientRepository;

    @Autowired
    private DirtyCheckingService sut;

    private Long testPostId;

    @BeforeEach
    public void setUp() {
        postRepository.deleteAll();

        Post post = new Post();
        post.setTitle(OLD_TITLE);
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

    @Test
    public void 엔티티_로드() {
        // 브레이크포인트 1: 엔티티 로드 직전
        Post post = postRepository.findById(1L).get();

        // 브레이크포인트 2: 엔티티 로드 직후
        post.setTitle("변경된 제목");

        // 브레이크포인트 3: 변경 직후
        // 이 시점에서는 아직 SQL이 생성되지 않음
    }

    @Test
    @Transactional
    public void 자동_플러시_로직() {
        // 브레이크포인트 1: 엔티티 변경 전
        Post post = postRepository.findById(1L).get();
        post.setTitle("변경된 제목");

        // 브레이크포인트 2: JPQL 쿼리 실행 직전 (자동 플러시 발생 지점)
        List<Post> allPosts = postRepository.findAll();

        // 브레이크포인트 3: 쿼리 실행 후
    }

    @Test
    @Transactional
    public void 더티체킹_로직() {
        // 브레이크포인트 1: 트랜잭션 시작
        Post post = postRepository.findById(1L).get();

        // 브레이크포인트 2: 여러 필드 변경
        post.setTitle("새 제목");

        // 브레이크포인트 3: 트랜잭션 종료 직전 (커밋 시점)
    } // 트랜잭션 종료 - 자동 플러시 발생

    @Test
    public void 준영속_상태_엔티티_변경은_감지되지_않음() {
        sut.updateDetached(testPostId, NEW_TITLE);

        Post actual = postRepository.findById(testPostId).get();

        assertEquals(OLD_TITLE, actual.getTitle());
    }

    @Test
    public void 비영속_필드_변경은_감지되지_않음() {
        PostWithTransient post = new PostWithTransient();
        post.setTitle(OLD_TITLE);
        post.setId(testPostId);
        postWithTransientRepository.save(post);

        PostWithTransient result = sut.updateTransientFieldAndReturn(testPostId, NEW_TITLE);

        assertEquals(NEW_TITLE, result.getTitle());
        assertEquals(NEW_TITLE, result.getTransientTitle());

        PostWithTransient reloaded = sut.findPostWithTransientById(testPostId);
        assertEquals(NEW_TITLE, reloaded.getTitle());
        assertNull(reloaded.getTransientTitle());
    }
}