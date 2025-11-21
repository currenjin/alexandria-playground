package com.currenjin.music.playlist.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSong.PlaylistSongId> {

    List<PlaylistSong> findByIdPlaylistId(Long playlistId);

    @Query("SELECT ps.id.songId FROM PlaylistSong ps WHERE ps.id.playlistId = :playlistId")
    List<Long> findSongIdsByPlaylistId(Long playlistId);
}
