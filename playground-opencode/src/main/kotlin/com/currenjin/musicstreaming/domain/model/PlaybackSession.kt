package com.currenjin.musicstreaming.domain.model

import java.time.Duration
import java.time.Instant

data class PlaybackSession(
    val id: PlaybackSessionId,
    val userId: UserId,
    val currentTrackId: TrackId,
    val queue: List<TrackId> = emptyList(),
    val currentPosition: Duration = Duration.ZERO,
    val state: PlaybackState = PlaybackState.PAUSED,
    val startedAt: Instant = Instant.now(),
) {
    fun play(): PlaybackSession = copy(state = PlaybackState.PLAYING)

    fun pause(): PlaybackSession = copy(state = PlaybackState.PAUSED)

    fun stop(): PlaybackSession = copy(
        state = PlaybackState.STOPPED,
        currentPosition = Duration.ZERO
    )

    fun seekTo(position: Duration): PlaybackSession = copy(currentPosition = position)

    fun nextTrack(): PlaybackSession? {
        if (queue.isEmpty()) return null
        return copy(
            currentTrackId = queue.first(),
            queue = queue.drop(1),
            currentPosition = Duration.ZERO,
            state = PlaybackState.PLAYING
        )
    }

    fun addToQueue(trackId: TrackId): PlaybackSession = copy(queue = queue + trackId)

    fun addToQueueNext(trackId: TrackId): PlaybackSession = copy(queue = listOf(trackId) + queue)

    fun clearQueue(): PlaybackSession = copy(queue = emptyList())

    fun isPlaying(): Boolean = state == PlaybackState.PLAYING
}

@JvmInline
value class PlaybackSessionId(val value: String)

enum class PlaybackState {
    PLAYING, PAUSED, STOPPED
}
