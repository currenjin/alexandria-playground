package com.currenjin.application.dto;

public class PostDto {
	public Long id;
	public String title;

	public PostDto(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
}
