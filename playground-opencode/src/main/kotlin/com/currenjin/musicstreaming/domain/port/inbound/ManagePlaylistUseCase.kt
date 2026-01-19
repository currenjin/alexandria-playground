package com.currenjin.musicstreaming.domain.port.inbound

import com.currenjin.musicstreaming.domain.model.*

interface ManagePlaylistUseCase {
    fun createPlaylist(userId: UserId, name: String): Playlist
    fun getPlaylist(playlistId: PlaylistId): Playlist?
    fun getUserPlaylists(userId: UserId): List<Playlist>
    fun addTrackToPlaylist(playlistId: PlaylistId, trackId: TrackId, userId: UserId): Playlist
    fun removeTrackFromPlaylist(playlistId: PlaylistId, trackId: TrackId, userId: UserId): Playlist
    fun renamePlaylist(playlistId: PlaylistId, newName: String, userId: UserId): Playlist
    fun deletePlaylist(playlistId: PlaylistId, userId: UserId)
    fun makePlaylistPublic(playlistId: PlaylistId, userId: UserId): Playlist
    fun makePlaylistPrivate(playlistId: PlaylistId, userId: UserId): Playlist
}
