package com.currenjin.musicstreaming.adapter.outbound.persistence

import com.currenjin.musicstreaming.domain.model.*
import com.currenjin.musicstreaming.domain.port.outbound.TrackRepository

class InMemoryTrackRepository : TrackRepository {
    private val tracks = mutableMapOf<TrackId, Track>()

    override fun findById(id: TrackId): Track? = tracks[id]

    override fun findByIds(ids: List<TrackId>): List<Track> =
        ids.mapNotNull { tracks[it] }

    override fun findByAlbumId(albumId: AlbumId): List<Track> =
        tracks.values.filter { it.albumId == albumId }

    override fun findByArtistId(artistId: ArtistId): List<Track> =
        tracks.values.filter { it.artistId == artistId }

    override fun findByGenre(genre: Genre, limit: Int): List<Track> =
        tracks.values.filter { it.genre == genre }.take(limit)

    override fun findTopByPlayCount(limit: Int): List<Track> =
        tracks.values.sortedByDescending { it.playCount }.take(limit)

    override fun search(query: String, limit: Int): List<Track> {
        val lowerQuery = query.lowercase()
        return tracks.values
            .filter { it.title.lowercase().contains(lowerQuery) }
            .take(limit)
    }

    override fun save(track: Track): Track {
        tracks[track.id] = track
        return track
    }

    override fun incrementPlayCount(trackId: TrackId): Track? {
        val track = tracks[trackId] ?: return null
        val updated = track.incrementPlayCount()
        tracks[trackId] = updated
        return updated
    }
}
