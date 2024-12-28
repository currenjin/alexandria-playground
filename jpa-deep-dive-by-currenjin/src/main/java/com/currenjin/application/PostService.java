package com.currenjin.application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.application.dto.PostDto;
import com.currenjin.domain.Post;
import com.currenjin.infrastucture.PostRepository;

@Service
@Transactional
public class PostService {
	private final PostRepository repository;

	public PostService(PostRepository repository) {
		this.repository = repository;
	}

	public void updatePost(PostDto dto) {
		Post post = new Post();
		post.setId(dto.getId());
		post.setTitle(dto.getTitle());

		repository.save(post);
	}

	public void updateFoundPost(PostDto dto) {
		Post post = repository.findById(dto.getId()).orElseThrow();

		post.setTitle(dto.getTitle());

		repository.save(post);
	}
}
