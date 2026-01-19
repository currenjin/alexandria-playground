package com.currenjin.musicstreaming.adapter.outbound.persistence

import com.currenjin.musicstreaming.domain.model.PlaybackSession
import com.currenjin.musicstreaming.domain.model.PlaybackSessionId
import com.currenjin.musicstreaming.domain.model.UserId
import com.currenjin.musicstreaming.domain.port.outbound.PlaybackSessionRepository

class InMemoryPlaybackSessionRepository : PlaybackSessionRepository {
    private val sessions = mutableMapOf<PlaybackSessionId, PlaybackSession>()

    override fun findById(id: PlaybackSessionId): PlaybackSession? = sessions[id]

    override fun findByUserId(userId: UserId): PlaybackSession? =
        sessions.values.find { it.userId == userId }

    override fun save(session: PlaybackSession): PlaybackSession {
        sessions[session.id] = session
        return session
    }

    override fun delete(id: PlaybackSessionId) {
        sessions.remove(id)
    }
}
