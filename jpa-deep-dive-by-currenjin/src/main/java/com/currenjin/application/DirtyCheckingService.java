package com.currenjin.application;

import com.currenjin.domain.Post;
import com.currenjin.domain.PostWithTransient;
import com.currenjin.infrastucture.PostRepository;
import com.currenjin.infrastucture.PostWithTransientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class DirtyCheckingService {
	@Autowired
	PostRepository postRepository;

	@Autowired
	PostWithTransientRepository postWithTransientRepository;

	@PersistenceContext
	private EntityManager em;

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

	@Transactional
	public void updateDetached(Long postId, String newTitle) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("Post not found"));

		em.detach(post);

		post.setTitle(newTitle);
	}

	@Transactional
	public void updateTransientField(Long postId, String newTitle) {
		PostWithTransient post = postWithTransientRepository.findById(postId)
				.orElseThrow(() -> new IllegalArgumentException("Post not found"));

		post.setTransientTitle(newTitle);
		post.setTitle(newTitle);
	}

	@Transactional
	public PostWithTransient updateTransientFieldAndReturn(Long postId, String newTitle) {
		PostWithTransient post = postWithTransientRepository.findById(postId)
				.orElseThrow(() -> new IllegalArgumentException("Post not found"));

		post.setTransientTitle(newTitle);
		post.setTitle(newTitle);

		return post;
	}

	@Transactional(readOnly = true)
	public PostWithTransient findPostWithTransientById(Long id) {
		return postWithTransientRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Post not found"));
	}
}
