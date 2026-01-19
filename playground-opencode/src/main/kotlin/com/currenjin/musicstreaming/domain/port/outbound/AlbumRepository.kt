package com.currenjin.musicstreaming.domain.port.outbound

import com.currenjin.musicstreaming.domain.model.Album
import com.currenjin.musicstreaming.domain.model.AlbumId
import com.currenjin.musicstreaming.domain.model.ArtistId

interface AlbumRepository {
    fun findById(id: AlbumId): Album?
    fun findByArtistId(artistId: ArtistId): List<Album>
    fun search(query: String, limit: Int): List<Album>
    fun save(album: Album): Album
}
