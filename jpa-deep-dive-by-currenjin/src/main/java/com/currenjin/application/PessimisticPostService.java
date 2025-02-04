package com.currenjin.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.domain.Post;
import com.currenjin.infrastucture.PostRepository;
import com.currenjin.util.Sleep;

@Service
public class PessimisticPostService {
	@Autowired
	private PostRepository repository;

	@Transactional
	public void pessimisticLockTest(Long postId) {
		Post post = repository.findByIdWithLock(postId).orElseThrow();
		System.out.println("Thread A - 조회된 Post 제목: " + post.getTitle());

		Sleep.delay(5000);

		post.setTitle("비관적 락 테스트 - 변경된 제목");
		repository.save(post);
		System.out.println("Thread A - 변경 후 저장 완료");
	}

	@Transactional
	public void pessimisticLockBlockingTest(Long postId) {
		Sleep.delay(1000);

		Post post = repository.findByIdWithLock(postId).orElseThrow();
		System.out.println("Thread B - 조회된 Post 제목: " + post.getTitle());

		post.setTitle("비관적 락 테스트 - 충돌 발생");
		repository.save(post);
		System.out.println("Thread B - 변경 후 저장 완료");
	}
}
