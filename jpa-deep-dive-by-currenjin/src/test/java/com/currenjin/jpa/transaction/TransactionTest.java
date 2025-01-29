package com.currenjin.jpa.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.currenjin.domain.Comment;
import com.currenjin.domain.Post;
import com.currenjin.infrastucture.CommentRepository;
import com.currenjin.infrastucture.PostRepository;

@SpringBootTest
class TransactionTest {

	@Autowired
	PostRepository postRepository;

	@Autowired
	CommentRepository commentRepository;

	@Test
	void 트랜잭션이_없으면_일부_데이터만_롤백된다() {
		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);
		Long postId = post.getId();

		try {
			Comment comment = new Comment();
			comment.setPost(post);
			comment.setContent("댓글1");
			commentRepository.save(comment);

			post.setTitle("제목2");
			throw new RuntimeException("예외 발생!");
		} catch (RuntimeException e) {
			Post foundPost = postRepository.findById(postId).get();
			assertEquals("제목1", foundPost.getTitle());
			assertEquals(1, commentRepository.findByPostId(postId).size());
		}
	}
}
