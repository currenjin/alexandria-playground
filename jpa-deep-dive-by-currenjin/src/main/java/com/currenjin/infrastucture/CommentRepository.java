package com.currenjin.infrastucture;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.currenjin.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPostId(Long postId);
}
