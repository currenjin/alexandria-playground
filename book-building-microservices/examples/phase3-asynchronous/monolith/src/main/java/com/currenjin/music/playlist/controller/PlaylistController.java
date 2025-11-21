package com.currenjin.music.playlist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.currenjin.music.client.song.dto.SongDto;
import com.currenjin.music.playlist.domain.Playlist;
import com.currenjin.music.playlist.service.PlaylistService;

import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<List<SongDto>> getPlaylistSongs(@PathVariable Long id) {
        List<SongDto> songs = playlistService.findSongsInPlaylist(id);
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
