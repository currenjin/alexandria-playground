package com.currenjin.music.playlist.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_song")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistSong {

    @EmbeddedId
    private PlaylistSongId id;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    public PlaylistSong(Long playlistId, Long songId) {
        this.id = new PlaylistSongId(playlistId, songId);
        this.addedAt = LocalDateTime.now();
    }

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaylistSongId implements Serializable {

        @Column(name = "playlist_id")
        private Long playlistId;

        @Column(name = "song_id")
        private Long songId;
    }
}
