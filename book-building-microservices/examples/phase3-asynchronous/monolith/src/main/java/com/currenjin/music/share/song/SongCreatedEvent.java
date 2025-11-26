package com.currenjin.music.share.song;

import com.currenjin.music.share.DomainEvent;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class SongCreatedEvent implements DomainEvent {
	java.lang.String type;
	LocalDateTime occurredAt;

	Long id;
	java.lang.String title;
	java.lang.String artist;
	Integer durationSeconds;
	java.lang.String genre;

	public SongCreatedEvent(Long id, java.lang.String title, java.lang.String artist, Integer durationSeconds, java.lang.String genre) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.durationSeconds = durationSeconds;
		this.genre = genre;
		this.type = this.getClass().getSimpleName();
		this.occurredAt = LocalDateTime.now();
	}
}
