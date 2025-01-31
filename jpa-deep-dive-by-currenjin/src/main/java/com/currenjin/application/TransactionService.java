package com.currenjin.application;

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
		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);

		Comment comment = new Comment();
		comment.setPost(post);
		comment.setContent("댓글1");
		commentRepository.save(comment);

		post.setTitle("제목2");
		throw new RuntimeException("예외 발생!");
	}

	@Transactional
	public void withTransaction() {
		Post post = new Post();
		post.setTitle("제목1");
		postRepository.save(post);

		Comment comment = new Comment();
		comment.setPost(post);
		comment.setContent("댓글1");
		commentRepository.save(comment);

		throw new RuntimeException("예외 발생!");
	}

	@PersistenceContext
	private EntityManager em;

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void insertPost() {
		Post post = new Post();
		post.setTitle("제목1");
		em.persist(post);
		em.flush();
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public boolean hasPost() {
		return em.createQuery("select count(p) from Post p", Long.class)
			.getSingleResult() > 0;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public boolean hasPostCommitted() {
		return em.createQuery("select count(p) from Post p", Long.class)
			.getSingleResult() > 0;
	}

}
