package com.currenjin.jpa.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.currenjin.application.OptimisticPostService;
import com.currenjin.application.PessimisticPostService;
import com.currenjin.domain.Post;
import com.currenjin.domain.PostWithVersion;
import com.currenjin.infrastucture.PostRepository;
import com.currenjin.infrastucture.PostWithVersionRepository;

@SpringBootTest
class LockTest {

	@Autowired
	OptimisticPostService optimisticPostService;

	@Autowired
	PessimisticPostService pessimisticPostService;

	@Autowired
	PostWithVersionRepository postWithVersionRepository;

	@Autowired
	PostRepository postRepository;

	@Test
	void 낙관적_락_테스트() throws InterruptedException {
		PostWithVersion post = new PostWithVersion();
		post.setTitle("초기 제목");
		postWithVersionRepository.save(post);

		Thread threadA = new Thread(() -> optimisticPostService.optimisticLockTest(post.getId()));
		Thread threadB = new Thread(() -> optimisticPostService.optimisticLockConflictTest(post.getId()));

		threadA.start();
		threadB.start();

		threadA.join();
		threadB.join();
	}

	@Test
	void 비관적_락_테스트() throws InterruptedException {
		Post post = new Post();
		post.setTitle("초기 제목");
		postRepository.save(post);

		Thread threadA = new Thread(() -> pessimisticPostService.pessimisticLockTest(post.getId()));
		Thread threadB = new Thread(() -> pessimisticPostService.pessimisticLockBlockingTest(post.getId()));

		threadA.start();
		threadB.start();

		threadA.join();
		threadB.join();
	}
}
