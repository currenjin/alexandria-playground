package com.currenjin.music.infrastructure.client.song.dto;

public record SongDto(
	Long id,
	String title,
	String artist,
	Integer durationSeconds,
	String genre
) { }
