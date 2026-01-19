package com.currenjin.musicstreaming.domain.model

data class Artist(
    val id: ArtistId,
    val name: String,
    val biography: String = "",
    val imageUrl: String? = null,
)

@JvmInline
value class ArtistId(val value: String)
