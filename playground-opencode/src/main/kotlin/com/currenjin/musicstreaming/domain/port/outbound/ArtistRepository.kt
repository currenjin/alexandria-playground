package com.currenjin.musicstreaming.domain.port.outbound

import com.currenjin.musicstreaming.domain.model.Artist
import com.currenjin.musicstreaming.domain.model.ArtistId

interface ArtistRepository {
    fun findById(id: ArtistId): Artist?
    fun search(query: String, limit: Int): List<Artist>
    fun save(artist: Artist): Artist
}
