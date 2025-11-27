package com.currenjin.music.streaming.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.music.infrastructure.client.song.SongClient;
import com.currenjin.music.infrastructure.client.song.dto.SongDto;
import com.currenjin.music.infrastructure.client.user.UserClient;
import com.currenjin.music.streaming.domain.PlayHistory;
import com.currenjin.music.streaming.domain.PlayHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StreamingService {

    private final PlayHistoryRepository playHistoryRepository;

    private final UserClient userClient;
    private final SongClient songClient;

    public List<PlayHistory> findByUserId(Long userId) {
        log.debug("Finding play history by userId: {}", userId);
        return playHistoryRepository.findByUserId(userId);
    }

    public List<PlayHistory> findRecentPlaysByUser(Long userId, int days) {
        log.debug("Finding recent plays by userId: {}, days: {}", userId, days);
        LocalDateTime after = LocalDateTime.now().minusDays(days);
        return playHistoryRepository.findByUserIdAndPlayedAtAfter(userId, after);
    }

    public Map<Long, Long> getMostPlayedSongs(int limit) {
        log.debug("Finding most played songs, limit: {}", limit);

        List<Object[]> results = playHistoryRepository.findMostPlayedSongs();

        return results.stream()
                .limit(limit)
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
    }

    public Map<Long, Long> getMostPlayedSongsByUser(Long userId, int limit) {
        log.debug("Finding most played songs by userId: {}, limit: {}", userId, limit);

        List<Object[]> results = playHistoryRepository.findMostPlayedSongsByUser(userId);

        return results.stream()
                .limit(limit)
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
    }

    @Transactional
    public PlayHistory recordPlay(Long userId, Long songId) {
        log.info("Recording play: userId={}, songId={}", userId, songId);

        if (!userClient.userExists(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        SongDto song = songClient.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found: " + songId));

        PlayHistory playHistory = new PlayHistory(userId, songId, song.durationSeconds());
        return playHistoryRepository.save(playHistory);
    }
}
