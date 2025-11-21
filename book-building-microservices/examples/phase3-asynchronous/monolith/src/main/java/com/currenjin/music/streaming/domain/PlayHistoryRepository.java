package com.currenjin.music.streaming.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {

    List<PlayHistory> findByUserId(Long userId);

    List<PlayHistory> findByUserIdAndPlayedAtAfter(Long userId, LocalDateTime after);

    @Query("SELECT ph.songId, COUNT(ph) as playCount FROM PlayHistory ph " +
            "GROUP BY ph.songId ORDER BY playCount DESC")
    List<Object[]> findMostPlayedSongs();

    @Query("SELECT ph.songId, COUNT(ph) as playCount FROM PlayHistory ph " +
            "WHERE ph.userId = :userId " +
            "GROUP BY ph.songId ORDER BY playCount DESC")
    List<Object[]> findMostPlayedSongsByUser(Long userId);
}
