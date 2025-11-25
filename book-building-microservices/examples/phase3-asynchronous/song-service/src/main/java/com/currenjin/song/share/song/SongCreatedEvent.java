package com.currenjin.song.share.song;

import com.currenjin.song.share.DomainEvent;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class SongCreatedEvent implements DomainEvent {
	String type;
	LocalDateTime occurredAt;

	Long id;
	String title;
	String artist;
	Integer durationSeconds;
	String genre;

	public SongCreatedEvent(Long id, String title, String artist, Integer durationSeconds, String genre) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.durationSeconds = durationSeconds;
		this.genre = genre;
		this.type = this.getClass().getSimpleName();
		this.occurredAt = LocalDateTime.now();
	}
}
