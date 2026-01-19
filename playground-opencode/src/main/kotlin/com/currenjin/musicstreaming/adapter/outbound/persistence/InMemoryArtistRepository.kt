package com.currenjin.musicstreaming.adapter.outbound.persistence

import com.currenjin.musicstreaming.domain.model.Artist
import com.currenjin.musicstreaming.domain.model.ArtistId
import com.currenjin.musicstreaming.domain.port.outbound.ArtistRepository

class InMemoryArtistRepository : ArtistRepository {
    private val artists = mutableMapOf<ArtistId, Artist>()

    override fun findById(id: ArtistId): Artist? = artists[id]

    override fun search(query: String, limit: Int): List<Artist> {
        val lowerQuery = query.lowercase()
        return artists.values
            .filter { it.name.lowercase().contains(lowerQuery) }
            .take(limit)
    }

    override fun save(artist: Artist): Artist {
        artists[artist.id] = artist
        return artist
    }
}
