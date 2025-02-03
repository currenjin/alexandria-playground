package com.currenjin.jpa.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.currenjin.application.PostWithVersionService;
import com.currenjin.domain.Post;
import com.currenjin.domain.PostWithVersion;
import com.currenjin.infrastucture.PostWithVersionRepository;

@SpringBootTest
class LockTest {

	@Autowired
	PostWithVersionService service;

	@Autowired
	PostWithVersionRepository repository;

	@Test
	void 낙관적_락_테스트() throws InterruptedException {
		PostWithVersion post = new PostWithVersion();
		post.setTitle("초기 제목");
		repository.save(post);

		Thread threadA = new Thread(() -> service.optimisticLockTest(post.getId()));
		Thread threadB = new Thread(() -> service.optimisticLockConflictTest(post.getId()));

		threadA.start();
		threadB.start();

		threadA.join();
		threadB.join();
	}
}
