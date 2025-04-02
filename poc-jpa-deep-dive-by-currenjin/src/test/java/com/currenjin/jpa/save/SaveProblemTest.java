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
    public static final Long ID = 1L;
    public static final String TITLE = "post";

    PostService sut;

    PostRepository repository;

    @SpyBean
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
        repository = factory.getRepository(PostRepository.class);

        sut = new PostService(repository);
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
        Post savedPost = savePostById(ID);
        PostDto postDto = createPostDtoByIdAndTitle(savedPost.getId(), TITLE);

        sut.updatePost(postDto);

        verify(entityManager, atLeastOnce()).merge(any(Post.class));
    }


    // Case 2: 엔티티 값이 없을 때
    // 1. ID 값이 없으면 새로 생성
    @Test
    @Transactional
    void case_2_persist() {
        Post savedPost = savePostById(ID);
        long beforeCount = repository.count();
        PostDto newPostDto = new PostDto(savedPost.getId() + 1L, TITLE);

        sut.updatePost(newPostDto);

        assertEquals(beforeCount + 1L, repository.count());
        verify(entityManager, atLeastOnce()).merge(any(Post.class));
    }

    // Case 1, 2 Solution: 의도치 않은 생성 또는 필드 교체 방지
    // 1. 조회 후 Save 또는 더티체킹으로 UPDATE!
    // 2. 트랜잭션 종료 시 Title만 변경!
    @Test
    @Transactional
    void case_1_2_solution() {
        Post savedPost = savePostById(ID);
        PostDto postDto = createPostDtoByIdAndTitle(savedPost.getId(), TITLE);

        sut.updateFoundPost(postDto);

        verify(entityManager, atLeastOnce()).merge(any(Post.class));
    }

    // Case 1, 2 Solution: 의도치 않은 생성 또는 필드 교체 방지
    // 1. ID 값이 없는 경우 Throw
    @Test
    @Transactional
    void case_1_2_solution_throw() {
        PostDto notFoundPostDto = createPostDtoByIdAndTitle(ID, TITLE);

        assertThrows(RuntimeException.class, () -> sut.updateFoundPost(notFoundPostDto));
    }

    // Case 3: 저장 시 ID값 직접 설정할 때
    // 1. Merge가 호출되면서 불필요한 SELECT 쿼리가 날아감
    @Test
    @Transactional
    void case_3_save_with_id_is_merge() {
        sut.saveWithId(1L);

        verify(entityManager, atLeastOnce()).merge(any(Post.class));
    }

    // Case 3 Solution: 불필요 쿼리 호출 제거
    // 1. ID 생성은 DB에게 위임
    // 2. persist 메소드를 통해 insert 쿼리 한 번만 호출
    @Test
    @Transactional
    void case_3_solution() {
        sut.saveWithoutId();

        verify(entityManager, never()).merge(any(Post.class));
        verify(entityManager, atLeastOnce()).persist(any(Post.class));
    }

    private Post savePostById(Long id) {
        Post post = new Post();
        post.setId(id);
        return repository.save(post);
    }

    private static PostDto createPostDtoByIdAndTitle(Long id, String title) {
        return new PostDto(id, title);
    }
}
