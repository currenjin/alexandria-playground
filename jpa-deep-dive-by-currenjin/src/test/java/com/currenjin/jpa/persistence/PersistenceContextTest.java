package com.currenjin.jpa.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;

import org.hibernate.Session;
import org.hibernate.engine.internal.StatefulPersistenceContext;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.engine.spi.Status;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.domain.Comment;
import com.currenjin.domain.Post;

@SpringBootTest
public class PersistenceContextTest {
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	void 영속성_컨텍스트_기본_동작() {
		Post post = new Post();
		assertThat(entityManager.contains(post)).isFalse();

		entityManager.persist(post);
		assertThat(entityManager.contains(post)).isTrue();

		entityManager.flush();
	}

	@Test
	@Transactional
	void 영속성_컨텍스트_구조() {
		SharedSessionContractImplementor session = entityManager.unwrap(SharedSessionContractImplementor.class);

		Post post = new Post();
		entityManager.persist(post);
		entityManager.flush();

		StatefulPersistenceContext pc = (StatefulPersistenceContext) session.getPersistenceContext();
		EntityEntry entry = pc.getEntry(post);

		assertEquals(Status.MANAGED, entry.getStatus());
		assertThat(entry.getEntityName()).contains("Post");
	}

	@Test
	@Transactional
	void 일차_캐시_테스트() {
		// 1. 엔티티 저장 시 1차 캐시에 저장됨
		Post post = new Post();
		entityManager.persist(post);

		// 2. 조회 시 1차 캐시에서 조회
		Post cachedPost = entityManager.find(Post.class, post.getId());  // SELECT 쿼리 발생 안함
		assertEquals(post, cachedPost);

		// 3. 1차 캐시에 없는 엔티티는 DB에서 조회
		Post dbPost = entityManager.find(Post.class, 999L);  // SELECT 쿼리 발생
		assertNull(dbPost);

		// 4. 1차 캐시 초기화
		entityManager.clear();
		Post reloadedPost = entityManager.find(Post.class, post.getId());  // SELECT 쿼리 발생
		assertNotEquals(reloadedPost, post);
	}

	@Test
	@Transactional
	void 쓰기_지연_테스트() {
		Post post1 = new Post();
		Post post2 = new Post();

		entityManager.persist(post1);
		entityManager.persist(post2);

		// 1. persist 호출 후에는 DB에 데이터가 없어야 함
		Session session = entityManager.unwrap(Session.class);
		assertEqualsCountWithSession(session, 0);

		// 2. flush 후에는 DB에 데이터가 있어야 함
		entityManager.flush();
		assertEqualsCountWithSession(session, 2);
	}

	private void assertEqualsCountWithSession(Session session, int firstExpected) {
		session.doWork(connection -> {
			try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM post")) {
				long count = getCount(ps);
				assertEquals(firstExpected, count);
			}
		});
	}

	private long getCount(PreparedStatement ps) throws SQLException {
		ResultSet resultSet = ps.executeQuery();
		resultSet.next();
		return resultSet.getLong(1);
	}

	@Test
	@Transactional
	void 변경_감지_테스트() {
		String firstTitle = "제목1";
		String secondTitle = "제목2";
		Post post = new Post();
		post.setTitle(firstTitle);

		entityManager.persist(post);
		entityManager.flush();

		// 1. DB에서 현재 데이터 확인
		Session session = entityManager.unwrap(Session.class);
		assertEqualsTitleByIdWithSession(session, post.getId(), firstTitle);

		// 2. 엔티티 수정
		post.setTitle(secondTitle);
		entityManager.flush();

		// 3. DB에서 변경된 데이터 확인
		assertEqualsTitleByIdWithSession(session, post.getId(), secondTitle);
	}

	private static void assertEqualsTitleByIdWithSession(Session session, Long id, String title) {
		session.doWork(connection -> {
			try (PreparedStatement ps = connection.prepareStatement("SELECT title FROM post WHERE id = ?")) {
				String selectedTitle = selectTitle(id, ps);
				assertEquals(title, selectedTitle);
			}
		});
	}

	private static String selectTitle(Long id, PreparedStatement ps) throws SQLException {
		ps.setLong(1, id);
		ResultSet resultSet = ps.executeQuery();
		resultSet.next();
		return resultSet.getString("title");
	}

	@Test
	@Transactional
	void 지연_로딩_테스트() {
		PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

		Post post = new Post();
		entityManager.persist(post);

		Comment comment1 = new Comment();
		comment1.setPost(post);
		entityManager.persist(comment1);

		Comment comment2 = new Comment();
		comment2.setPost(post);
		entityManager.persist(comment2);

		entityManager.flush();
		entityManager.clear();

		Post foundPost = entityManager.find(Post.class, post.getId());  // SELECT post만 실행

		// comments는 아직 초기화되지 않음
		assertFalse(persistenceUnitUtil.isLoaded(foundPost, "comments"));

		// comments 실제 호출 & 초기화 완료
		assertEquals(2, foundPost.getComments().size());
		assertTrue(persistenceUnitUtil.isLoaded(foundPost, "comments"));
	}
}
