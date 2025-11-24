package com.currenjin.song.domain.event;

public record SongCreatedEvent(
	Long id,
	String title,
	String artist,
	Integer durationSeconds,
	String genre
) implements DomainEvent { }
