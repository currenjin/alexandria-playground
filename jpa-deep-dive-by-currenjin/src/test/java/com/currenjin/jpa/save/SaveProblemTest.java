package com.currenjin.jpa.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.application.PostService;
import com.currenjin.application.dto.PostDto;
import com.currenjin.domain.Post;
import com.currenjin.infrastucture.PostRepository;

@SpringBootTest
class SaveProblemTest {
    public static final long ID = 1L;
    public static final String TITLE = "post";

    private final Post post = new Post();
    private final PostDto postDto = new PostDto(ID, TITLE);

    PostService sut;

    PostRepository repository;

    @SpyBean
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
        repository = factory.getRepository(PostRepository.class);

        sut = new PostService(repository);

        post.setId(ID);
        repository.save(post);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    // Case 1: 준영속 엔티티를 수정할 때
    // 1. ID 수정 이후 Save를 하면, Merge 호출!
    // 2. 모든 필드가 새로운 Entity 값으로 교체!
    @Test
    @Transactional
    void case_1() {
        sut.updatePost(postDto);

        verify(entityManager, atLeastOnce()).merge(any(Post.class));
    }

    // Case 2: 엔티티 값이 없을 때
    // 1. ID 값이 없으면 새로 생성
    @Test
    @Transactional
    void case_2_persist() {
        PostDto newPostDto = new PostDto(ID + 1L, TITLE);
        long beforeCount = repository.count();

        sut.updatePost(newPostDto);

        assertEquals(beforeCount + 1L, repository.count());
        verify(entityManager, atLeastOnce()).merge(any(Post.class));
    }

    // Case Solution
    // 1. 조회 후 Save 또는 더티체킹으로 UPDATE!
    // 2. 트랜잭션 종료 시 Title만 변경!
    @Test
    @Transactional
    void case_solution() {
        sut.updateFoundPost(postDto);

        verify(entityManager, atLeastOnce()).merge(any(Post.class));
    }

    // Case Solution
    // 1. ID 값이 없는 경우 Throw
    @Test
    @Transactional
    void case_solution_throw() {
        PostDto notFoundPostDto = new PostDto(ID + 1L, TITLE);

        assertThrows(RuntimeException.class, () -> sut.updateFoundPost(notFoundPostDto));
    }
}
