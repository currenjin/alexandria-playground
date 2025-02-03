package com.currenjin.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.domain.Post;
import com.currenjin.infrastucture.PostRepository;

@Service
public class PessimisticPostService {
	@Autowired
	private PostRepository repository;

	@Transactional
	public void pessimisticLockTest(Long postId) {
		Post post = repository.findByIdWithLock(postId).orElseThrow();
		System.out.println("Thread A - 조회된 Post 제목: " + post.getTitle());

		sleep(5000);

		post.setTitle("비관적 락 테스트 - 변경된 제목");
		repository.save(post);
		System.out.println("Thread A - 변경 후 저장 완료");
	}

	@Transactional
	public void pessimisticLockBlockingTest(Long postId) {
		sleep(1000);

		Post post = repository.findByIdWithLock(postId).orElseThrow();
		System.out.println("Thread B - 조회된 Post 제목: " + post.getTitle());

		post.setTitle("비관적 락 테스트 - 충돌 발생");
		repository.save(post);
		System.out.println("Thread B - 변경 후 저장 완료");
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
