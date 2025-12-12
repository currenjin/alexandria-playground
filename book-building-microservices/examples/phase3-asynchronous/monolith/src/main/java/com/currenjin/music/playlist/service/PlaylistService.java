package com.currenjin.music.playlist.service;

import com.currenjin.music.infrastructure.client.song.SongClient;
import com.currenjin.music.infrastructure.client.song.dto.SongDto;
import com.currenjin.music.infrastructure.client.user.UserClient;
import com.currenjin.music.playlist.domain.Playlist;
import com.currenjin.music.playlist.domain.PlaylistRepository;
import com.currenjin.music.playlist.domain.PlaylistSong;
import com.currenjin.music.playlist.domain.PlaylistSongRepository;
import com.currenjin.music.share.song.CachedSong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;

    private final UserClient userClient;
    private final SongClient songClient;

    public List<Playlist> findAll() {
        log.debug("Finding all playlists");
        return playlistRepository.findAll();
    }

    public Playlist findById(Long id) {
        log.debug("Finding playlist by id: {}", id);
        return playlistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found: " + id));
    }

    public List<Playlist> findByUserId(Long userId) {
        log.debug("Finding playlists by userId: {}", userId);
        return playlistRepository.findByUserId(userId);
    }

    public List<CachedSong> findSongsInPlaylist(Long playlistId) {
        log.debug("Finding songs in playlist: {}", playlistId);

        playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found: " + playlistId));

        List<Long> songIds = playlistSongRepository.findSongIdsByPlaylistId(playlistId);

        return songClient.findAllBySongIds(songIds);
    }

    @Transactional
    public Playlist create(Long userId, String name) {
        log.info("Creating playlist: userId={}, name={}", userId, name);

        if (!userClient.userExists(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        Playlist playlist = new Playlist(userId, name);
        return playlistRepository.save(playlist);
    }

    @Transactional
    public void addSongToPlaylist(Long playlistId, Long songId) {
        log.info("Adding song to playlist: playlistId={}, songId={}", playlistId, songId);

        playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found: " + playlistId));

		if (!songClient.songExists(songId)) {
			throw new IllegalArgumentException("Song not found: " + songId);
		}

        PlaylistSong.PlaylistSongId id = new PlaylistSong.PlaylistSongId(playlistId, songId);
        if (playlistSongRepository.existsById(id)) {
            throw new IllegalArgumentException("Song already exists in playlist");
        }

        PlaylistSong playlistSong = new PlaylistSong(playlistId, songId);
        playlistSongRepository.save(playlistSong);
    }

    @Transactional
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        log.info("Removing song from playlist: playlistId={}, songId={}", playlistId, songId);

        PlaylistSong.PlaylistSongId id = new PlaylistSong.PlaylistSongId(playlistId, songId);
        playlistSongRepository.deleteById(id);
    }
}
