package com.currenjin.song.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.song.domain.Song;
import com.currenjin.song.domain.SongRepository;
import com.currenjin.song.domain.event.SongCreatedEvent;
import com.currenjin.song.service.publisher.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SongService {

    private final SongRepository songRepository;
	private final EventPublisher eventPublisher;

    public List<Song> findAll() {
        log.debug("Finding all songs");
        return songRepository.findAll();
    }

    public Song findById(Long id) {
        log.debug("Finding song by id: {}", id);
        return songRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Song not found: " + id));
    }

	public List<Song> findByIds(List<Long> ids) {
		log.debug("Finding songs by ids: {}", ids);
		return songRepository.findAllById(ids);
	}

    public List<Song> findByGenre(String genre) {
        log.debug("Finding songs by genre: {}", genre);
        return songRepository.findByGenre(genre);
    }

    public List<Song> findByArtist(String artist) {
        log.debug("Finding songs by artist: {}", artist);
        return songRepository.findByArtist(artist);
    }

    @Transactional
    public Song create(String title, String artist, Integer durationSeconds, String genre) {
        log.info("Creating song: title={}, artist={}, duration={}, genre={}",
                title, artist, durationSeconds, genre);

        Song song = new Song(title, artist, durationSeconds, genre);

		SongCreatedEvent event = new SongCreatedEvent(song);
		eventPublisher.publish(event);

        return songRepository.save(song);
    }
}
