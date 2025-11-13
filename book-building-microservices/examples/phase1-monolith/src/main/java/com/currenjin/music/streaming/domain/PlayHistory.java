package com.currenjin.music.streaming.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "play_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "song_id", nullable = false)
    private Long songId;

    @Column(name = "played_at", nullable = false)
    private LocalDateTime playedAt;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    public PlayHistory(Long userId, Long songId, Integer durationSeconds) {
        this.userId = userId;
        this.songId = songId;
        this.playedAt = LocalDateTime.now();
        this.durationSeconds = durationSeconds;
    }
}
