package com.currenjin.application;

import javax.persistence.OptimisticLockException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.domain.PostWithVersion;
import com.currenjin.infrastucture.PostWithVersionRepository;

@Service
public class PostWithVersionService {
	@Autowired
	private PostWithVersionRepository repository;

	@Transactional
	public void optimisticLockTest(Long postId) {
		PostWithVersion post = repository.findById(postId).orElseThrow();
		System.out.println("[Thread A] 조회된 Post 제목: " + post.getTitle());

		sleep(3000);

		post.setTitle("낙관적 락 테스트 - 변경된 제목");
		repository.save(post);
		System.out.println("[Thread A] 변경 후 저장 완료");
	}

	@Transactional
	public void optimisticLockConflictTest(Long postId) {
		sleep(1000);

		PostWithVersion post = repository.findById(postId).orElseThrow();
		System.out.println("[Thread B] 조회된 Post 제목: " + post.getTitle());

		sleep(3000);

		post.setTitle("낙관적 락 테스트 - 충돌 발생");
		try {
			repository.save(post);
			System.out.println("[Thread B] 변경 후 저장 완료");
		} catch (OptimisticLockException e) {
			System.out.println("[Thread B] 낙관적 락 충돌 발생!");
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
