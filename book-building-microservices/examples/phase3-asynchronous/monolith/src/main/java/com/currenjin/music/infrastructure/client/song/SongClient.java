package com.currenjin.music.infrastructure.client.song;

import java.util.List;
import java.util.Optional;

import com.currenjin.music.infrastructure.client.song.dto.SongDto;
import com.currenjin.music.share.song.CachedSong;

public interface SongClient {
    Boolean songExists(Long songId);

	List<CachedSong> findAllBySongIds(List<Long> songIds);

	Optional<CachedSong> findById(Long songId);
}
