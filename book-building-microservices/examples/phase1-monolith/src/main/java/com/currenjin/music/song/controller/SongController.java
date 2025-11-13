package com.currenjin.music.song.controller;

import com.currenjin.music.song.domain.Song;
import com.currenjin.music.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String artist) {

        List<Song> songs;
        if (genre != null) {
            songs = songService.findByGenre(genre);
        } else if (artist != null) {
            songs = songService.findByArtist(artist);
        } else {
            songs = songService.findAll();
        }

        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSong(@PathVariable Long id) {
        Song song = songService.findById(id);
        return ResponseEntity.ok(song);
    }

    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody CreateSongRequest request) {
        Song song = songService.create(
                request.getTitle(),
                request.getArtist(),
                request.getDurationSeconds(),
                request.getGenre()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(song);
    }

    @lombok.Data
    public static class CreateSongRequest {
        private String title;
        private String artist;
        private Integer durationSeconds;
        private String genre;
    }
}
