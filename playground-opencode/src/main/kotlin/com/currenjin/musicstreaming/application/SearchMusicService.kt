package com.currenjin.musicstreaming.application

import com.currenjin.musicstreaming.domain.model.*
import com.currenjin.musicstreaming.domain.port.inbound.SearchMusicUseCase
import com.currenjin.musicstreaming.domain.port.inbound.SearchResult
import com.currenjin.musicstreaming.domain.port.outbound.AlbumRepository
import com.currenjin.musicstreaming.domain.port.outbound.ArtistRepository
import com.currenjin.musicstreaming.domain.port.outbound.TrackRepository

class SearchMusicService(
    private val trackRepository: TrackRepository,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
) : SearchMusicUseCase {

    override fun searchTracks(query: String, limit: Int): List<Track> {
        return trackRepository.search(query, limit)
    }

    override fun searchAlbums(query: String, limit: Int): List<Album> {
        return albumRepository.search(query, limit)
    }

    override fun searchArtists(query: String, limit: Int): List<Artist> {
        return artistRepository.search(query, limit)
    }

    override fun searchAll(query: String, limit: Int): SearchResult {
        return SearchResult(
            tracks = searchTracks(query, limit),
            albums = searchAlbums(query, limit),
            artists = searchArtists(query, limit),
        )
    }

    override fun getTracksByGenre(genre: Genre, limit: Int): List<Track> {
        return trackRepository.findByGenre(genre, limit)
    }

    override fun getTopTracks(limit: Int): List<Track> {
        return trackRepository.findTopByPlayCount(limit)
    }

    override fun getArtistTracks(artistId: ArtistId): List<Track> {
        artistRepository.findById(artistId)
            ?: throw MusicStreamingError.ArtistNotFound(artistId)

        return trackRepository.findByArtistId(artistId)
    }

    override fun getAlbumTracks(albumId: AlbumId): List<Track> {
        albumRepository.findById(albumId)
            ?: throw MusicStreamingError.AlbumNotFound(albumId)

        return trackRepository.findByAlbumId(albumId)
    }
}
