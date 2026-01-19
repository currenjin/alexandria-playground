package com.currenjin.musicstreaming.adapter.inbound.rest

import com.currenjin.musicstreaming.adapter.inbound.rest.dto.*
import com.currenjin.musicstreaming.domain.model.PlaybackSessionId
import com.currenjin.musicstreaming.domain.model.TrackId
import com.currenjin.musicstreaming.domain.model.UserId
import com.currenjin.musicstreaming.domain.port.inbound.PlayMusicUseCase
import java.time.Duration

class PlaybackController(
    private val playMusicUseCase: PlayMusicUseCase,
) {
    fun playTrack(request: PlayTrackRequest): PlaybackSessionResponse {
        val session = playMusicUseCase.playTrack(
            userId = UserId(request.userId),
            trackId = TrackId(request.trackId),
        )
        return PlaybackSessionResponse.from(session)
    }

    fun pausePlayback(sessionId: String): PlaybackSessionResponse {
        val session = playMusicUseCase.pausePlayback(PlaybackSessionId(sessionId))
        return PlaybackSessionResponse.from(session)
    }

    fun resumePlayback(sessionId: String): PlaybackSessionResponse {
        val session = playMusicUseCase.resumePlayback(PlaybackSessionId(sessionId))
        return PlaybackSessionResponse.from(session)
    }

    fun stopPlayback(sessionId: String): PlaybackSessionResponse {
        val session = playMusicUseCase.stopPlayback(PlaybackSessionId(sessionId))
        return PlaybackSessionResponse.from(session)
    }

    fun seekTo(sessionId: String, request: SeekRequest): PlaybackSessionResponse {
        val session = playMusicUseCase.seekTo(
            sessionId = PlaybackSessionId(sessionId),
            position = Duration.ofSeconds(request.positionSeconds),
        )
        return PlaybackSessionResponse.from(session)
    }

    fun skipToNext(sessionId: String): PlaybackSessionResponse? {
        val session = playMusicUseCase.skipToNext(PlaybackSessionId(sessionId))
        return session?.let { PlaybackSessionResponse.from(it) }
    }

    fun addToQueue(sessionId: String, request: AddToQueueRequest): PlaybackSessionResponse {
        val session = playMusicUseCase.addToQueue(
            sessionId = PlaybackSessionId(sessionId),
            trackId = TrackId(request.trackId),
        )
        return PlaybackSessionResponse.from(session)
    }

    fun getPlaybackSession(sessionId: String): PlaybackSessionResponse? {
        val session = playMusicUseCase.getPlaybackSession(PlaybackSessionId(sessionId))
        return session?.let { PlaybackSessionResponse.from(it) }
    }
}
