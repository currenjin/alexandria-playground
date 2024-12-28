package com.currenjin.infrastucture;

import org.springframework.data.jpa.repository.JpaRepository;

import com.currenjin.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
