package com.currenjin.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {
	@Id
	@GeneratedValue
	private Long id;
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	private Post post;

	public void setPost(Post post) {
		this.post = post;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
