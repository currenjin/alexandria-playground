package com.currenjin.musicstreaming.domain.port.inbound

import com.currenjin.musicstreaming.domain.model.*

interface SearchMusicUseCase {
    fun searchTracks(query: String, limit: Int = 20): List<Track>
    fun searchAlbums(query: String, limit: Int = 20): List<Album>
    fun searchArtists(query: String, limit: Int = 20): List<Artist>
    fun searchAll(query: String, limit: Int = 10): SearchResult
    fun getTracksByGenre(genre: Genre, limit: Int = 50): List<Track>
    fun getTopTracks(limit: Int = 50): List<Track>
    fun getArtistTracks(artistId: ArtistId): List<Track>
    fun getAlbumTracks(albumId: AlbumId): List<Track>
}

data class SearchResult(
    val tracks: List<Track>,
    val albums: List<Album>,
    val artists: List<Artist>,
)
