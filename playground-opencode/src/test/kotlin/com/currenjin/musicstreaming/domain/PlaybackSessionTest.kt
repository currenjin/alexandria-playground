package com.currenjin.musicstreaming.domain

import com.currenjin.musicstreaming.domain.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Duration

class PlaybackSessionTest {

    @Test
    fun new_session_is_paused_by_default() {
        val session = createSession()

        assertEquals(PlaybackState.PAUSED, session.state)
        assertFalse(session.isPlaying())
    }

    @Test
    fun play_changes_state_to_playing() {
        val session = createSession()

        val playing = session.play()

        assertEquals(PlaybackState.PLAYING, playing.state)
        assertTrue(playing.isPlaying())
    }

    @Test
    fun pause_changes_state_to_paused() {
        val session = createSession().play()

        val paused = session.pause()

        assertEquals(PlaybackState.PAUSED, paused.state)
        assertFalse(paused.isPlaying())
    }

    @Test
    fun stop_resets_position_and_state() {
        val session = createSession()
            .play()
            .seekTo(Duration.ofSeconds(30))

        val stopped = session.stop()

        assertEquals(PlaybackState.STOPPED, stopped.state)
        assertEquals(Duration.ZERO, stopped.currentPosition)
    }

    @Test
    fun seek_to_changes_position() {
        val session = createSession()
        val newPosition = Duration.ofSeconds(60)

        val seeked = session.seekTo(newPosition)

        assertEquals(newPosition, seeked.currentPosition)
    }

    @Test
    fun next_track_moves_to_first_in_queue() {
        val track2 = TrackId("track-2")
        val track3 = TrackId("track-3")
        val session = createSession()
            .addToQueue(track2)
            .addToQueue(track3)

        val next = session.nextTrack()

        assertNotNull(next)
        assertEquals(track2, next!!.currentTrackId)
        assertEquals(listOf(track3), next.queue)
        assertEquals(Duration.ZERO, next.currentPosition)
        assertTrue(next.isPlaying())
    }

    @Test
    fun next_track_returns_null_when_queue_empty() {
        val session = createSession()

        val next = session.nextTrack()

        assertNull(next)
    }

    @Test
    fun add_to_queue_appends_track() {
        val track2 = TrackId("track-2")
        val track3 = TrackId("track-3")
        val session = createSession()

        val updated = session
            .addToQueue(track2)
            .addToQueue(track3)

        assertEquals(listOf(track2, track3), updated.queue)
    }

    @Test
    fun add_to_queue_next_prepends_track() {
        val track2 = TrackId("track-2")
        val track3 = TrackId("track-3")
        val session = createSession().addToQueue(track2)

        val updated = session.addToQueueNext(track3)

        assertEquals(listOf(track3, track2), updated.queue)
    }

    @Test
    fun clear_queue_removes_all_tracks() {
        val session = createSession()
            .addToQueue(TrackId("track-2"))
            .addToQueue(TrackId("track-3"))

        val cleared = session.clearQueue()

        assertTrue(cleared.queue.isEmpty())
    }

    private fun createSession(): PlaybackSession = PlaybackSession(
        id = PlaybackSessionId("session-1"),
        userId = UserId("user-1"),
        currentTrackId = TrackId("track-1"),
    )
}
