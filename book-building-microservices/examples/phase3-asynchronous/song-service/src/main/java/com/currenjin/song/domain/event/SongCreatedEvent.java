package com.currenjin.song.domain.event;

import java.time.LocalDateTime;

import com.currenjin.song.domain.Song;

import lombok.Value;

@Value
public class SongCreatedEvent implements DomainEvent {
	String type;
	LocalDateTime occurredAt;

	Long id;
	String title;
	String artist;
	Integer durationSeconds;
	String genre;

	public SongCreatedEvent(Song song) {
		this.id = song.getId();
		this.title = song.getTitle();
		this.artist = song.getArtist();
		this.durationSeconds = song.getDurationSeconds();
		this.genre = song.getGenre();
		this.type = this.getClass().getSimpleName();
		this.occurredAt = LocalDateTime.now();
	}
}
