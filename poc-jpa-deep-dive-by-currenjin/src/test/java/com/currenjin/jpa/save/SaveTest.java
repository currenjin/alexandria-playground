package com.currenjin.jpa.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import com.currenjin.domain.Post;
import com.currenjin.infrastucture.PostRepository;

@SpringBootTest
class SaveTest {
    PostRepository repository;

    @SpyBean
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
        repository = factory.getRepository(PostRepository.class);
    }

    @Test
    @Transactional
    void save_메소드는_ID가_없으면_persist를_호출한다() {
        Post post = new Post();
        post.setTitle("제목1");

        repository.save(post);

        verify(entityManager).persist(post);
        verify(entityManager, never()).merge(post);
    }

    @Test
    @Transactional
    void save_메소드는_ID가_있으면_merge를_호출한다() {
        Post post = new Post();
        post.setId(2L);

        repository.save(post);

        verify(entityManager, never()).persist(post);
        verify(entityManager).merge(post);
    }

    @Test
    @Transactional
    void persist에서_반환한_인스턴스는_동일하다() {
        Post post = new Post();

        Post savedPost = repository.save(post);

        verify(entityManager).persist(post);
        assertEquals(post, savedPost);
    }

    @Test
    @Transactional
    void merge에서_반환한_인스턴스는_다르다() {
        Post post = new Post();
        post.setId(1L);

        Post savedPost = repository.save(post);

        verify(entityManager).merge(post);
        assertNotEquals(post, savedPost);
    }
}
