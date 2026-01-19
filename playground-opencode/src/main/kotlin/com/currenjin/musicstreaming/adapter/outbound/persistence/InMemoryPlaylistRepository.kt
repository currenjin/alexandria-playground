package com.currenjin.musicstreaming.adapter.outbound.persistence

import com.currenjin.musicstreaming.domain.model.Playlist
import com.currenjin.musicstreaming.domain.model.PlaylistId
import com.currenjin.musicstreaming.domain.model.UserId
import com.currenjin.musicstreaming.domain.port.outbound.PlaylistRepository

class InMemoryPlaylistRepository : PlaylistRepository {
    private val playlists = mutableMapOf<PlaylistId, Playlist>()

    override fun findById(id: PlaylistId): Playlist? = playlists[id]

    override fun findByUserId(userId: UserId): List<Playlist> =
        playlists.values.filter { it.ownerId == userId }

    override fun findPublicPlaylists(limit: Int): List<Playlist> =
        playlists.values.filter { it.isPublic }.take(limit)

    override fun save(playlist: Playlist): Playlist {
        playlists[playlist.id] = playlist
        return playlist
    }

    override fun delete(id: PlaylistId) {
        playlists.remove(id)
    }
}
