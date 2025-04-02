package com.currenjin.jpa.persistence;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.domain.Post;

@SpringBootTest
public class PersistenceContextLifeCycleTest {
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	void 엔티티_생명주기_테스트() {
		String firstTitle = "제목1";

		// 1. 비영속 (new/transient)
		Post post = new Post();
		post.setTitle(firstTitle);
		assertFalse(entityManager.contains(post));
		assertNull(post.getId());

		// 2. 영속 (managed)
		entityManager.persist(post);
		assertTrue(entityManager.contains(post));
		assertNotNull(post.getId());

		// DB 저장 확인
		entityManager.flush();
		entityManager.clear();

		Post managedPost = entityManager.find(Post.class, post.getId());
		assertNotNull(managedPost);

		// 3. 준영속 (detached)
		entityManager.detach(managedPost);
		assertFalse(entityManager.contains(managedPost));

		// 준영속 상태에서는 변경 감지가 동작하지 않음
		String updatedTitle = "변경된 제목";
		managedPost.setTitle(updatedTitle);
		entityManager.flush();

		Post foundPost = entityManager.find(Post.class, managedPost.getId());
		assertNotEquals(updatedTitle, foundPost.getTitle());

		// 4. 삭제 (removed)
		entityManager.remove(foundPost);
		assertFalse(entityManager.contains(foundPost));

		// DB에서도 삭제되었는지 확인
		entityManager.flush();
		assertNull(entityManager.find(Post.class, foundPost.getId()));
	}
}
