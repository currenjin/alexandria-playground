package com.currenjin.musicstreaming.domain.port.inbound

import com.currenjin.musicstreaming.domain.model.*
import java.time.Duration

interface PlayMusicUseCase {
    fun playTrack(userId: UserId, trackId: TrackId): PlaybackSession
    fun pausePlayback(sessionId: PlaybackSessionId): PlaybackSession
    fun resumePlayback(sessionId: PlaybackSessionId): PlaybackSession
    fun stopPlayback(sessionId: PlaybackSessionId): PlaybackSession
    fun seekTo(sessionId: PlaybackSessionId, position: Duration): PlaybackSession
    fun skipToNext(sessionId: PlaybackSessionId): PlaybackSession?
    fun addToQueue(sessionId: PlaybackSessionId, trackId: TrackId): PlaybackSession
    fun getPlaybackSession(sessionId: PlaybackSessionId): PlaybackSession?
}
