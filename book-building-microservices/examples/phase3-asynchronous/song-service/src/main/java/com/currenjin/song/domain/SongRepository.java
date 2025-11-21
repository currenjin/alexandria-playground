package com.currenjin.song.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> findByGenre(String genre);

    List<Song> findByArtist(String artist);

	Iterable<Long> id(Long id);
}
