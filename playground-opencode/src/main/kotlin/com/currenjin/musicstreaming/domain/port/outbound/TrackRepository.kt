package com.currenjin.musicstreaming.domain.port.outbound

import com.currenjin.musicstreaming.domain.model.*

interface TrackRepository {
    fun findById(id: TrackId): Track?
    fun findByIds(ids: List<TrackId>): List<Track>
    fun findByAlbumId(albumId: AlbumId): List<Track>
    fun findByArtistId(artistId: ArtistId): List<Track>
    fun findByGenre(genre: Genre, limit: Int): List<Track>
    fun findTopByPlayCount(limit: Int): List<Track>
    fun search(query: String, limit: Int): List<Track>
    fun save(track: Track): Track
    fun incrementPlayCount(trackId: TrackId): Track?
}
