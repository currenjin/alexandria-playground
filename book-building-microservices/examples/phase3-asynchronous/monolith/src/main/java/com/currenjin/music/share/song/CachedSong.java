package com.currenjin.music.share.song;

public record CachedSong (
	Long id,
	String title,
	String artist,
	Integer durationSeconds,
	String genre
) { }