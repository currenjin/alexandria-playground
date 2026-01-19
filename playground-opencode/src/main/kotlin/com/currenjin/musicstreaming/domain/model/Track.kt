package com.currenjin.musicstreaming.domain.model

import java.time.Duration

data class Track(
    val id: TrackId,
    val title: String,
    val artistId: ArtistId,
    val albumId: AlbumId,
    val duration: Duration,
    val audioUrl: String,
    val genre: Genre = Genre.UNKNOWN,
    val playCount: Long = 0,
) {
    fun incrementPlayCount(): Track =
        copy(playCount = playCount + 1)

    fun durationInSeconds(): Long = duration.seconds
}

@JvmInline
value class TrackId(val value: String)

enum class Genre {
    POP, ROCK, HIPHOP, RNB, JAZZ, CLASSICAL, ELECTRONIC, COUNTRY, FOLK, INDIE, METAL, UNKNOWN
}
