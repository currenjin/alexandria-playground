package com.currenjin.musicstreaming.application

import com.currenjin.musicstreaming.adapter.outbound.persistence.InMemoryPlaybackSessionRepository
import com.currenjin.musicstreaming.adapter.outbound.persistence.InMemoryTrackRepository
import com.currenjin.musicstreaming.adapter.outbound.persistence.InMemoryUserRepository
import com.currenjin.musicstreaming.domain.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration

class PlayMusicServiceTest {

    private lateinit var playbackSessionRepository: InMemoryPlaybackSessionRepository
    private lateinit var trackRepository: InMemoryTrackRepository
    private lateinit var userRepository: InMemoryUserRepository
    private lateinit var service: PlayMusicService

    private val userId = UserId("user-1")
    private val trackId = TrackId("track-1")

    @BeforeEach
    fun setup() {
        playbackSessionRepository = InMemoryPlaybackSessionRepository()
        trackRepository = InMemoryTrackRepository()
        userRepository = InMemoryUserRepository()
        service = PlayMusicService(playbackSessionRepository, trackRepository, userRepository)

        userRepository.save(User(
            id = userId,
            email = "test@example.com",
            displayName = "Test User"
        ))

        trackRepository.save(Track(
            id = trackId,
            title = "Test Track",
            artistId = ArtistId("artist-1"),
            albumId = AlbumId("album-1"),
            duration = Duration.ofMinutes(3),
            audioUrl = "http://example.com/track.mp3"
        ))
    }

    @Test
    fun play_track_creates_new_session() {
        val session = service.playTrack(userId, trackId)

        assertNotNull(session)
        assertEquals(trackId, session.currentTrackId)
        assertEquals(userId, session.userId)
        assertTrue(session.isPlaying())
    }

    @Test
    fun play_track_increments_play_count() {
        service.playTrack(userId, trackId)

        val track = trackRepository.findById(trackId)
        assertEquals(1, track!!.playCount)
    }

    @Test
    fun play_track_throws_when_user_not_found() {
        val unknownUser = UserId("unknown")

        assertThrows(MusicStreamingError.UserNotFound::class.java) {
            service.playTrack(unknownUser, trackId)
        }
    }

    @Test
    fun play_track_throws_when_track_not_found() {
        val unknownTrack = TrackId("unknown")

        assertThrows(MusicStreamingError.TrackNotFound::class.java) {
            service.playTrack(userId, unknownTrack)
        }
    }

    @Test
    fun pause_playback_changes_state() {
        val session = service.playTrack(userId, trackId)

        val paused = service.pausePlayback(session.id)

        assertFalse(paused.isPlaying())
    }

    @Test
    fun resume_playback_changes_state() {
        val session = service.playTrack(userId, trackId)
        val paused = service.pausePlayback(session.id)

        val resumed = service.resumePlayback(paused.id)

        assertTrue(resumed.isPlaying())
    }

    @Test
    fun seek_to_changes_position() {
        val session = service.playTrack(userId, trackId)
        val position = Duration.ofSeconds(60)

        val seeked = service.seekTo(session.id, position)

        assertEquals(position, seeked.currentPosition)
    }

    @Test
    fun seek_beyond_duration_throws() {
        val session = service.playTrack(userId, trackId)
        val invalidPosition = Duration.ofMinutes(10)

        assertThrows(MusicStreamingError.InvalidOperation::class.java) {
            service.seekTo(session.id, invalidPosition)
        }
    }

    @Test
    fun add_to_queue_appends_track() {
        val session = service.playTrack(userId, trackId)
        val track2 = TrackId("track-2")
        trackRepository.save(Track(
            id = track2,
            title = "Track 2",
            artistId = ArtistId("artist-1"),
            albumId = AlbumId("album-1"),
            duration = Duration.ofMinutes(4),
            audioUrl = "http://example.com/track2.mp3"
        ))

        val updated = service.addToQueue(session.id, track2)

        assertEquals(listOf(track2), updated.queue)
    }

    @Test
    fun skip_to_next_moves_to_queue_track() {
        val track2 = TrackId("track-2")
        trackRepository.save(Track(
            id = track2,
            title = "Track 2",
            artistId = ArtistId("artist-1"),
            albumId = AlbumId("album-1"),
            duration = Duration.ofMinutes(4),
            audioUrl = "http://example.com/track2.mp3"
        ))
        val session = service.playTrack(userId, trackId)
        service.addToQueue(session.id, track2)

        val next = service.skipToNext(session.id)

        assertNotNull(next)
        assertEquals(track2, next!!.currentTrackId)
    }

    @Test
    fun skip_to_next_returns_null_when_queue_empty() {
        val session = service.playTrack(userId, trackId)

        val next = service.skipToNext(session.id)

        assertNull(next)
    }
}
