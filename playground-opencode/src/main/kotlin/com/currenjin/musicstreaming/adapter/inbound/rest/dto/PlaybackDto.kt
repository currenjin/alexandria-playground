package com.currenjin.musicstreaming.adapter.inbound.rest.dto

import com.currenjin.musicstreaming.domain.model.PlaybackSession
import com.currenjin.musicstreaming.domain.model.PlaybackState

data class PlayTrackRequest(
    val userId: String,
    val trackId: String,
)

data class SeekRequest(
    val positionSeconds: Long,
)

data class AddToQueueRequest(
    val trackId: String,
)

data class PlaybackSessionResponse(
    val id: String,
    val userId: String,
    val currentTrackId: String,
    val queue: List<String>,
    val currentPositionSeconds: Long,
    val state: PlaybackState,
) {
    companion object {
        fun from(session: PlaybackSession): PlaybackSessionResponse = PlaybackSessionResponse(
            id = session.id.value,
            userId = session.userId.value,
            currentTrackId = session.currentTrackId.value,
            queue = session.queue.map { it.value },
            currentPositionSeconds = session.currentPosition.seconds,
            state = session.state,
        )
    }
}
