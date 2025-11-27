package com.currenjin.music.infrastructure.client.song;

import java.util.List;
import java.util.Optional;

import com.currenjin.music.infrastructure.client.song.dto.SongDto;

public interface SongClient {
    Boolean songExists(Long songId);

	List<SongDto> findAllBySongIds(List<Long> songIds);

	Optional<SongDto> findById(Long songId);
}
