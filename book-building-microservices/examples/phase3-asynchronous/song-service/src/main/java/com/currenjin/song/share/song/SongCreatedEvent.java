package com.currenjin.song.share.song;

import com.currenjin.song.domain.Song;
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
