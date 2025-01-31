package com.currenjin.jpa.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.currenjin.application.TransactionService;
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

		assertEquals("제목1", postRepository.findAll().stream().findFirst().get().getTitle());
		assertEquals(1, commentRepository.count());
	}

	@Test
	void 트랜잭션의_원자성_테스트() {
		assertThrows(RuntimeException.class, () -> transactionService.withTransaction());

		assertEquals(0, postRepository.count());
		assertEquals(0, commentRepository.count());
	}
}
