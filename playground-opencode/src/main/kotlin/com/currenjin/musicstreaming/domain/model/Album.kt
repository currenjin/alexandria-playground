package com.currenjin.musicstreaming.domain.model

import java.time.LocalDate

data class Album(
    val id: AlbumId,
    val title: String,
    val artistId: ArtistId,
    val releaseDate: LocalDate,
    val coverImageUrl: String? = null,
    val trackIds: List<TrackId> = emptyList(),
) {
    fun addTrack(trackId: TrackId): Album =
        copy(trackIds = trackIds + trackId)

    fun trackCount(): Int = trackIds.size
}

@JvmInline
value class AlbumId(val value: String)
