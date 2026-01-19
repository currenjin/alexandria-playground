package com.currenjin.musicstreaming.domain.model

sealed class MusicStreamingError(message: String) : RuntimeException(message) {
    
    class TrackNotFound(trackId: TrackId) : MusicStreamingError("Track not found: ${trackId.value}")
    
    class AlbumNotFound(albumId: AlbumId) : MusicStreamingError("Album not found: ${albumId.value}")
    
    class ArtistNotFound(artistId: ArtistId) : MusicStreamingError("Artist not found: ${artistId.value}")
    
    class PlaylistNotFound(playlistId: PlaylistId) : MusicStreamingError("Playlist not found: ${playlistId.value}")
    
    class UserNotFound(userId: UserId) : MusicStreamingError("User not found: ${userId.value}")
    
    class UnauthorizedAccess(message: String) : MusicStreamingError(message)
    
    class PlaybackError(message: String) : MusicStreamingError(message)
    
    class PremiumRequired(feature: String) : MusicStreamingError("Premium subscription required for: $feature")
    
    class InvalidOperation(message: String) : MusicStreamingError(message)
}
