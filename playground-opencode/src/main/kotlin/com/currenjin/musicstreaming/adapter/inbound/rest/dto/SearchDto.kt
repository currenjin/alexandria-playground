package com.currenjin.musicstreaming.adapter.inbound.rest.dto

import com.currenjin.musicstreaming.domain.model.*
import com.currenjin.musicstreaming.domain.port.inbound.SearchResult
import java.time.LocalDate

data class TrackResponse(
    val id: String,
    val title: String,
    val artistId: String,
    val albumId: String,
    val durationSeconds: Long,
    val genre: Genre,
    val playCount: Long,
) {
    companion object {
        fun from(track: Track): TrackResponse = TrackResponse(
            id = track.id.value,
            title = track.title,
            artistId = track.artistId.value,
            albumId = track.albumId.value,
            durationSeconds = track.durationInSeconds(),
            genre = track.genre,
            playCount = track.playCount,
        )
    }
}

data class AlbumResponse(
    val id: String,
    val title: String,
    val artistId: String,
    val releaseDate: LocalDate,
    val coverImageUrl: String?,
    val trackCount: Int,
) {
    companion object {
        fun from(album: Album): AlbumResponse = AlbumResponse(
            id = album.id.value,
            title = album.title,
            artistId = album.artistId.value,
            releaseDate = album.releaseDate,
            coverImageUrl = album.coverImageUrl,
            trackCount = album.trackCount(),
        )
    }
}

data class ArtistResponse(
    val id: String,
    val name: String,
    val biography: String,
    val imageUrl: String?,
) {
    companion object {
        fun from(artist: Artist): ArtistResponse = ArtistResponse(
            id = artist.id.value,
            name = artist.name,
            biography = artist.biography,
            imageUrl = artist.imageUrl,
        )
    }
}

data class SearchResultResponse(
    val tracks: List<TrackResponse>,
    val albums: List<AlbumResponse>,
    val artists: List<ArtistResponse>,
) {
    companion object {
        fun from(result: SearchResult): SearchResultResponse = SearchResultResponse(
            tracks = result.tracks.map { TrackResponse.from(it) },
            albums = result.albums.map { AlbumResponse.from(it) },
            artists = result.artists.map { ArtistResponse.from(it) },
        )
    }
}
