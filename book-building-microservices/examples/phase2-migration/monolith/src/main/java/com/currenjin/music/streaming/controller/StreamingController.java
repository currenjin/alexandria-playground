package com.currenjin.music.streaming.controller;

import com.currenjin.music.streaming.domain.PlayHistory;
import com.currenjin.music.streaming.service.StreamingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/streaming")
@RequiredArgsConstructor
public class StreamingController {

    private final StreamingService streamingService;

    @PostMapping("/play")
    public ResponseEntity<PlayHistory> recordPlay(@RequestBody PlayRequest request) {
        PlayHistory playHistory = streamingService.recordPlay(request.getUserId(), request.getSongId());
        return ResponseEntity.status(HttpStatus.CREATED).body(playHistory);
    }

    @GetMapping("/history")
    public ResponseEntity<List<PlayHistory>> getPlayHistory(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "30") int days) {

        List<PlayHistory> history = streamingService.findRecentPlaysByUser(userId, days);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/statistics/most-played")
    public ResponseEntity<Map<Long, Long>> getMostPlayedSongs(
            @RequestParam(required = false, defaultValue = "10") int limit) {

        Map<Long, Long> mostPlayed = streamingService.getMostPlayedSongs(limit);
        return ResponseEntity.ok(mostPlayed);
    }

    @GetMapping("/statistics/most-played/user/{userId}")
    public ResponseEntity<Map<Long, Long>> getMostPlayedSongsByUser(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "10") int limit) {

        Map<Long, Long> mostPlayed = streamingService.getMostPlayedSongsByUser(userId, limit);
        return ResponseEntity.ok(mostPlayed);
    }

    @lombok.Data
    public static class PlayRequest {
        private Long userId;
        private Long songId;
    }
}
