package com.currenjin.musicstreaming.domain.port.outbound

import com.currenjin.musicstreaming.domain.model.PlaybackSession
import com.currenjin.musicstreaming.domain.model.PlaybackSessionId
import com.currenjin.musicstreaming.domain.model.UserId

interface PlaybackSessionRepository {
    fun findById(id: PlaybackSessionId): PlaybackSession?
    fun findByUserId(userId: UserId): PlaybackSession?
    fun save(session: PlaybackSession): PlaybackSession
    fun delete(id: PlaybackSessionId)
}
