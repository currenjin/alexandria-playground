package com.currenjin.jpa.transaction;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.currenjin.domain.Post;
import com.currenjin.infrastucture.PostRepository;

@SpringBootTest
class TransactionTest {
	@Autowired
	PostRepository postRepository;

	@PersistenceContext
	EntityManager em;

	@Autowired
	JpaTransactionManager transactionManager;

	@Test
	void 트랜잭션이_없으면_영속성_컨텍스트도_없다() {
		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);

		post.setTitle("제목2");

		Post foundPost = postRepository.findById(post.getId()).get();
		assertThat(foundPost.getTitle()).isEqualTo("제목1");
		assertFalse(em.contains(post));
	}

	@Test
	@Transactional
	void 트랜잭션_범위와_영속성_컨텍스트_범위는_같다() {
		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);

		post.setTitle("제목2");

		Post foundPost = postRepository.findById(post.getId()).get();
		assertThat(foundPost.getTitle()).isEqualTo("제목2");
		assertTrue(em.contains(post));
	}

	@Test
	void 트랜잭션_동작방식() {
		TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionDefinition());

		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);

		Post foundPost = postRepository.findById(post.getId()).get();
		assertThat(foundPost.getTitle()).isEqualTo("제목1");
		assertTrue(em.contains(post));

		transactionManager.commit(tx);
	}

	@Test
	void 트랜잭션_동작_원리() {
		// 1. 트랜잭션 시작
		TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionDefinition());

		// 2. 엔티티 저장
		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);
		assertTrue(em.contains(post));  // 영속성 컨텍스트에 저장됨

		// 3. 엔티티 수정
		post.setTitle("제목2");
		em.flush();

		// 4. 트랜잭션 커밋
		transactionManager.commit(tx);

		// 5. 다시 조회해서 변경사항 확인
		tx = transactionManager.getTransaction(new DefaultTransactionDefinition());
		Post found = postRepository.findById(post.getId()).get();
		assertEquals("제목2", found.getTitle());
		transactionManager.commit(tx);
	}

	@Test
	@Transactional
	void 트랜잭션_커밋_시점에_변경이_반영된다() {
		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);

		post.setTitle("제목2");
		em.flush();
		em.clear();

		Post foundPost = postRepository.findById(post.getId()).get();
		assertThat(foundPost.getTitle()).isEqualTo("제목2");
	}

	@Test
	void 트랜잭션_롤백_시_변경이_반영되지_않는다() {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);
		Long id = post.getId();

		post.setTitle("제목2");
		em.flush();

		transactionManager.rollback(status);

		status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		boolean exists = postRepository.existsById(id);
		assertFalse(exists);
		transactionManager.commit(status);
	}
}
