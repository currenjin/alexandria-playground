package com.currenjin.jpa.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.domain.Post;
import com.currenjin.infrastucture.PostRepository;

@SpringBootTest
class TransactionTest {

	@Autowired
	PostRepository postRepository;

	@PersistenceContext
	EntityManager em;

	@Test
	void 트랜잭션이_없으면_데이터_변경이_안된다() {
		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);

		post.setTitle("제목2");

		Post foundPost = postRepository.findById(post.getId()).get();
		assertEquals("제목1", foundPost.getTitle());
	}

	@Test
	@Transactional
	void 트랜잭션_안에서는_데이터_변경이_된다() {
		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);

		post.setTitle("제목2");

		Post foundPost = postRepository.findById(post.getId()).get();
		assertEquals("제목2", foundPost.getTitle());
	}
}
