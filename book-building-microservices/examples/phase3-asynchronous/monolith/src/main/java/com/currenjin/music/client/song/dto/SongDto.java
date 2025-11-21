package com.currenjin.music.client.song.dto;

public record SongDto(
	Long id,
	String title,
	String artist,
	Integer durationSeconds,
	String genre
) { }
