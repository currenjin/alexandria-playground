package com.currenjin.musicstreaming.adapter.outbound.persistence

import com.currenjin.musicstreaming.domain.model.Album
import com.currenjin.musicstreaming.domain.model.AlbumId
import com.currenjin.musicstreaming.domain.model.ArtistId
import com.currenjin.musicstreaming.domain.port.outbound.AlbumRepository

class InMemoryAlbumRepository : AlbumRepository {
    private val albums = mutableMapOf<AlbumId, Album>()

    override fun findById(id: AlbumId): Album? = albums[id]

    override fun findByArtistId(artistId: ArtistId): List<Album> =
        albums.values.filter { it.artistId == artistId }

    override fun search(query: String, limit: Int): List<Album> {
        val lowerQuery = query.lowercase()
        return albums.values
            .filter { it.title.lowercase().contains(lowerQuery) }
            .take(limit)
    }

    override fun save(album: Album): Album {
        albums[album.id] = album
        return album
    }
}
