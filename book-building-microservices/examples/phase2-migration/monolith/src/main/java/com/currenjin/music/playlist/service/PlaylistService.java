package com.currenjin.music.playlist.service;

import com.currenjin.music.playlist.domain.Playlist;
import com.currenjin.music.playlist.domain.PlaylistRepository;
import com.currenjin.music.playlist.domain.PlaylistSong;
import com.currenjin.music.playlist.domain.PlaylistSongRepository;
import com.currenjin.music.song.domain.Song;
import com.currenjin.music.song.domain.SongRepository;
import com.currenjin.music.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserRepository userRepository;
    private final SongRepository songRepository;

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

    public List<Song> findSongsInPlaylist(Long playlistId) {
        log.debug("Finding songs in playlist: {}", playlistId);

        playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found: " + playlistId));

        List<Long> songIds = playlistSongRepository.findSongIdsByPlaylistId(playlistId);

        return songRepository.findAllById(songIds);
    }

    @Transactional
    public Playlist create(Long userId, String name) {
        log.info("Creating playlist: userId={}, name={}", userId, name);

        // User 존재 확인 (모놀리스: 직접 접근)
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Playlist playlist = new Playlist(userId, name);
        return playlistRepository.save(playlist);
    }

    @Transactional
    public void addSongToPlaylist(Long playlistId, Long songId) {
        log.info("Adding song to playlist: playlistId={}, songId={}", playlistId, songId);

        playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found: " + playlistId));

        songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found: " + songId));

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
