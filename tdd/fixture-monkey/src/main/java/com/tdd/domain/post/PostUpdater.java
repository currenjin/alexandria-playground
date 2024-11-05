package com.tdd.domain.post;

import org.springframework.stereotype.Service;

@Service
public class PostUpdater {
    public PostUpdater(PostRepository repository) {
        this.repository = repository;
    }

    private PostRepository repository;

    public void updateTitleBy(Long id, String updatedTitle) {
        Post post = repository.findById(id).get();

        post.setTitle(updatedTitle);

        repository.save(post);
    }
}
