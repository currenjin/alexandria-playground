package com.currenjin.music.song.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> findByGenre(String genre);

    List<Song> findByArtist(String artist);
}
