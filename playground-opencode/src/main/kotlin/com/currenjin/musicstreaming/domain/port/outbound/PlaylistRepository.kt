package com.currenjin.musicstreaming.domain.port.outbound

import com.currenjin.musicstreaming.domain.model.Playlist
import com.currenjin.musicstreaming.domain.model.PlaylistId
import com.currenjin.musicstreaming.domain.model.UserId

interface PlaylistRepository {
    fun findById(id: PlaylistId): Playlist?
    fun findByUserId(userId: UserId): List<Playlist>
    fun findPublicPlaylists(limit: Int): List<Playlist>
    fun save(playlist: Playlist): Playlist
    fun delete(id: PlaylistId)
}
