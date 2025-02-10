package com.currenjin.jpa.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.currenjin.application.TransactionService;
import com.currenjin.domain.Post;
import com.currenjin.infrastucture.CommentRepository;
import com.currenjin.infrastucture.PostRepository;

@SpringBootTest
class TransactionTest {

	@Autowired
	TransactionService transactionService;

	@Autowired
	PostRepository postRepository;

	@Autowired
	CommentRepository commentRepository;

	@BeforeEach
	void setup() {
		commentRepository.deleteAll();
		postRepository.deleteAll();
	}

	@Test
	void 트랜잭션이_없으면_데이터_정합성이_깨진다() {
		assertThrows(RuntimeException.class, () -> transactionService.withoutTransaction());

		assertEquals(1, postRepository.count());
		assertEquals(1, commentRepository.count());
	}

	@Test
	void 트랜잭션이_있으면_데이터_정합성이_유지된다() {
		assertThrows(RuntimeException.class, () -> transactionService.withTransaction());

		assertEquals(0, postRepository.count());
		assertEquals(0, commentRepository.count());
	}

	@Test
	void 격리_수준이_READ_COMMITTED_인_경우_조회() throws InterruptedException {
		Post post = new Post();
		post.setTitle("A");
		postRepository.save(post);

		Thread threadA = new Thread(this::readCommittedTransaction);
		Thread threadB = new Thread(this::updatePostTitle);

		threadA.start();
		threadB.start();

		threadA.join();
		threadB.join();
	}

	@Test
	void 격리_수준이_REPEATABLE_READ_인_경우_조회() throws InterruptedException {
		Post post = new Post();
		post.setTitle("A");
		postRepository.save(post);

		Thread threadA = new Thread(this::repeatableReadTransaction);
		Thread threadB = new Thread(this::updatePostTitle);

		threadA.start();
		threadB.start();

		threadA.join();
		threadB.join();
	}

	private void readCommittedTransaction() {
		try {
			transactionService.readCommittedTransaction();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void repeatableReadTransaction() {
		try {
			transactionService.repeatableReadTransaction();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void updatePostTitle() {
		try {
			transactionService.updatePostTitle();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
