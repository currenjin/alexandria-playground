package com.currenjin.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.domain.Comment;
import com.currenjin.domain.Post;
import com.currenjin.infrastucture.CommentRepository;
import com.currenjin.infrastucture.PostRepository;

@Service
public class TransactionService {
	@Autowired
	PostRepository postRepository;

	@Autowired
	CommentRepository commentRepository;

	public void withoutTransaction() {
		createPostAndComment();
	}

	@Transactional
	public void withTransaction() {
		createPostAndComment();
	}

	private void createPostAndComment() {
		Post post = new Post();
		post.setTitle("트랜잭션 테스트");

		Comment comment1 = new Comment();
		comment1.setContent("첫 번째 댓글");
		comment1.setPost(post);

		Comment comment2 = new Comment();
		comment2.setContent("두 번째 댓글");
		comment2.setPost(post);

		postRepository.save(post);
		commentRepository.save(comment1);

		exception();

		commentRepository.save(comment2);
	}

	private static void exception() {
		throw new RuntimeException("예외 발생!");
	}
}
