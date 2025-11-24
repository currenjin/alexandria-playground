package com.currenjin.song.domain.event;

import java.time.LocalDateTime;

import com.currenjin.song.domain.Song;

import lombok.Value;

@Value
public class SongCreatedEvent implements DomainEvent {
	String topic;
	String type;
	LocalDateTime occurredAt;

	Long id;
	String title;
	String artist;
	Integer durationSeconds;
	String genre;

	public SongCreatedEvent(Song song, String type) {
		this.id = song.getId();
		this.title = song.getTitle();
		this.artist = song.getArtist();
		this.durationSeconds = song.getDurationSeconds();
		this.genre = song.getGenre();
		this.topic = this.getClass().getSimpleName();
		this.type = type;
		this.occurredAt = LocalDateTime.now();
	}
}
