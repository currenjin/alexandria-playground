package com.currenjin.application;

import static java.lang.Thread.sleep;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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

	@PersistenceContext
	EntityManager entityManager;

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void readCommittedTransaction() throws InterruptedException {
		Post post = postRepository.findById(1L).orElseThrow();
		System.out.println("조회된 Post 제목: " + post.getTitle());

		sleep(3000);
		entityManager.clear();

		Post foundPost = postRepository.findById(1L).orElseThrow();
		System.out.println("다시 조회된 Post 제목: " + foundPost.getTitle());
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void repeatableReadTransaction() throws InterruptedException {
		Post post = postRepository.findById(1L).orElseThrow();
		System.out.println("조회된 Post 제목: " + post.getTitle());

		sleep(3000);
		entityManager.clear();

		Post foundPost = postRepository.findById(1L).orElseThrow();
		System.out.println("다시 조회된 Post 제목: " + foundPost.getTitle());
	}

	public void updatePostTitle() throws InterruptedException {
		sleep(1000);

		Post post = postRepository.findAll().get(0);
		post.setTitle("B");
		postRepository.save(post);

		System.out.println("Thread B - 제목 변경 후 커밋 완료");
	}
}
