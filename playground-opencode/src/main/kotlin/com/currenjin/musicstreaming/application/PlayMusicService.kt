package com.currenjin.musicstreaming.application

import com.currenjin.musicstreaming.domain.model.*
import com.currenjin.musicstreaming.domain.port.inbound.PlayMusicUseCase
import com.currenjin.musicstreaming.domain.port.outbound.PlaybackSessionRepository
import com.currenjin.musicstreaming.domain.port.outbound.TrackRepository
import com.currenjin.musicstreaming.domain.port.outbound.UserRepository
import java.time.Duration
import java.util.UUID

class PlayMusicService(
    private val playbackSessionRepository: PlaybackSessionRepository,
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository,
) : PlayMusicUseCase {

    override fun playTrack(userId: UserId, trackId: TrackId): PlaybackSession {
        val user = userRepository.findById(userId)
            ?: throw MusicStreamingError.UserNotFound(userId)

        val track = trackRepository.findById(trackId)
            ?: throw MusicStreamingError.TrackNotFound(trackId)

        trackRepository.incrementPlayCount(trackId)

        val existingSession = playbackSessionRepository.findByUserId(userId)
        val session = if (existingSession != null) {
            existingSession.copy(
                currentTrackId = trackId,
                currentPosition = Duration.ZERO,
                state = PlaybackState.PLAYING
            )
        } else {
            PlaybackSession(
                id = PlaybackSessionId(UUID.randomUUID().toString()),
                userId = userId,
                currentTrackId = trackId,
                state = PlaybackState.PLAYING
            )
        }

        return playbackSessionRepository.save(session)
    }

    override fun pausePlayback(sessionId: PlaybackSessionId): PlaybackSession {
        val session = getSessionOrThrow(sessionId)
        return playbackSessionRepository.save(session.pause())
    }

    override fun resumePlayback(sessionId: PlaybackSessionId): PlaybackSession {
        val session = getSessionOrThrow(sessionId)
        return playbackSessionRepository.save(session.play())
    }

    override fun stopPlayback(sessionId: PlaybackSessionId): PlaybackSession {
        val session = getSessionOrThrow(sessionId)
        return playbackSessionRepository.save(session.stop())
    }

    override fun seekTo(sessionId: PlaybackSessionId, position: Duration): PlaybackSession {
        val session = getSessionOrThrow(sessionId)
        val track = trackRepository.findById(session.currentTrackId)
            ?: throw MusicStreamingError.TrackNotFound(session.currentTrackId)

        if (position > track.duration) {
            throw MusicStreamingError.InvalidOperation("Seek position exceeds track duration")
        }

        return playbackSessionRepository.save(session.seekTo(position))
    }

    override fun skipToNext(sessionId: PlaybackSessionId): PlaybackSession? {
        val session = getSessionOrThrow(sessionId)
        val nextSession = session.nextTrack() ?: return null

        trackRepository.incrementPlayCount(nextSession.currentTrackId)
        return playbackSessionRepository.save(nextSession)
    }

    override fun addToQueue(sessionId: PlaybackSessionId, trackId: TrackId): PlaybackSession {
        val session = getSessionOrThrow(sessionId)
        
        trackRepository.findById(trackId)
            ?: throw MusicStreamingError.TrackNotFound(trackId)

        return playbackSessionRepository.save(session.addToQueue(trackId))
    }

    override fun getPlaybackSession(sessionId: PlaybackSessionId): PlaybackSession? {
        return playbackSessionRepository.findById(sessionId)
    }

    private fun getSessionOrThrow(sessionId: PlaybackSessionId): PlaybackSession {
        return playbackSessionRepository.findById(sessionId)
            ?: throw MusicStreamingError.PlaybackError("Playback session not found: ${sessionId.value}")
    }
}
