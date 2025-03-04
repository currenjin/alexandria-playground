package com.currenjin.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.domain.Post;
import com.currenjin.infrastucture.PostRepository;

@Service
public class DirtyCheckingService {
	@Autowired
	PostRepository postRepository;

	@Transactional
	public void updateTitleWithSave(Long postId, String newTitle) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("Post not found"));

		post.setTitle(newTitle);

		postRepository.save(post);
	}

	@Transactional
	public void updateTitleWithoutSave(Long postId, String newTitle) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("Post not found"));

		post.setTitle(newTitle);
	}
}
