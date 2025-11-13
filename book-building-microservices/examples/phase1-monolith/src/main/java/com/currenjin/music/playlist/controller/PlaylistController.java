package com.currenjin.music.playlist.controller;

import com.currenjin.music.playlist.domain.Playlist;
import com.currenjin.music.playlist.service.PlaylistService;
import com.currenjin.music.song.domain.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getAllPlaylists(@RequestParam(required = false) Long userId) {
        List<Playlist> playlists = userId != null
                ? playlistService.findByUserId(userId)
                : playlistService.findAll();
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable Long id) {
        Playlist playlist = playlistService.findById(id);
        return ResponseEntity.ok(playlist);
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getPlaylistSongs(@PathVariable Long id) {
        List<Song> songs = playlistService.findSongsInPlaylist(id);
        return ResponseEntity.ok(songs);
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody CreatePlaylistRequest request) {
        Playlist playlist = playlistService.create(request.getUserId(), request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(playlist);
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> addSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> removeSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        playlistService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.noContent().build();
    }

    @lombok.Data
    public static class CreatePlaylistRequest {
        private Long userId;
        private String name;
    }
}
