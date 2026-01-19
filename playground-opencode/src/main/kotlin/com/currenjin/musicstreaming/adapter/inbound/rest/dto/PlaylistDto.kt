package com.currenjin.musicstreaming.adapter.inbound.rest.dto

import com.currenjin.musicstreaming.domain.model.Playlist
import java.time.Instant

data class CreatePlaylistRequest(
    val userId: String,
    val name: String,
)

data class AddTrackToPlaylistRequest(
    val userId: String,
    val trackId: String,
)

data class RemoveTrackFromPlaylistRequest(
    val userId: String,
    val trackId: String,
)

data class RenamePlaylistRequest(
    val userId: String,
    val newName: String,
)

data class PlaylistResponse(
    val id: String,
    val name: String,
    val ownerId: String,
    val trackIds: List<String>,
    val isPublic: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun from(playlist: Playlist): PlaylistResponse = PlaylistResponse(
            id = playlist.id.value,
            name = playlist.name,
            ownerId = playlist.ownerId.value,
            trackIds = playlist.trackIds.map { it.value },
            isPublic = playlist.isPublic,
            createdAt = playlist.createdAt,
            updatedAt = playlist.updatedAt,
        )
    }
}
