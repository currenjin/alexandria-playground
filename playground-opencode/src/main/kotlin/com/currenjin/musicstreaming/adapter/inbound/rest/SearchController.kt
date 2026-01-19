package com.currenjin.musicstreaming.adapter.inbound.rest

import com.currenjin.musicstreaming.adapter.inbound.rest.dto.*
import com.currenjin.musicstreaming.domain.model.AlbumId
import com.currenjin.musicstreaming.domain.model.ArtistId
import com.currenjin.musicstreaming.domain.model.Genre
import com.currenjin.musicstreaming.domain.port.inbound.SearchMusicUseCase

class SearchController(
    private val searchMusicUseCase: SearchMusicUseCase,
) {
    fun searchAll(query: String, limit: Int = 10): SearchResultResponse {
        val result = searchMusicUseCase.searchAll(query, limit)
        return SearchResultResponse.from(result)
    }

    fun searchTracks(query: String, limit: Int = 20): List<TrackResponse> {
        return searchMusicUseCase.searchTracks(query, limit)
            .map { TrackResponse.from(it) }
    }

    fun searchAlbums(query: String, limit: Int = 20): List<AlbumResponse> {
        return searchMusicUseCase.searchAlbums(query, limit)
            .map { AlbumResponse.from(it) }
    }

    fun searchArtists(query: String, limit: Int = 20): List<ArtistResponse> {
        return searchMusicUseCase.searchArtists(query, limit)
            .map { ArtistResponse.from(it) }
    }

    fun getTracksByGenre(genre: Genre, limit: Int = 50): List<TrackResponse> {
        return searchMusicUseCase.getTracksByGenre(genre, limit)
            .map { TrackResponse.from(it) }
    }

    fun getTopTracks(limit: Int = 50): List<TrackResponse> {
        return searchMusicUseCase.getTopTracks(limit)
            .map { TrackResponse.from(it) }
    }

    fun getArtistTracks(artistId: String): List<TrackResponse> {
        return searchMusicUseCase.getArtistTracks(ArtistId(artistId))
            .map { TrackResponse.from(it) }
    }

    fun getAlbumTracks(albumId: String): List<TrackResponse> {
        return searchMusicUseCase.getAlbumTracks(AlbumId(albumId))
            .map { TrackResponse.from(it) }
    }
}
