package com.currenjin.musicstreaming.domain.model

import java.time.Instant

data class Playlist(
    val id: PlaylistId,
    val name: String,
    val ownerId: UserId,
    val trackIds: List<TrackId> = emptyList(),
    val isPublic: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
) {
    fun addTrack(trackId: TrackId): Playlist {
        if (trackIds.contains(trackId)) {
            return this
        }
        return copy(
            trackIds = trackIds + trackId,
            updatedAt = Instant.now()
        )
    }

    fun removeTrack(trackId: TrackId): Playlist =
        copy(
            trackIds = trackIds - trackId,
            updatedAt = Instant.now()
        )

    fun reorderTracks(newOrder: List<TrackId>): Playlist {
        require(newOrder.toSet() == trackIds.toSet()) { "New order must contain same tracks" }
        return copy(
            trackIds = newOrder,
            updatedAt = Instant.now()
        )
    }

    fun makePublic(): Playlist = copy(isPublic = true, updatedAt = Instant.now())

    fun makePrivate(): Playlist = copy(isPublic = false, updatedAt = Instant.now())

    fun rename(newName: String): Playlist = copy(name = newName, updatedAt = Instant.now())

    fun trackCount(): Int = trackIds.size

    fun isEmpty(): Boolean = trackIds.isEmpty()
}

@JvmInline
value class PlaylistId(val value: String)
