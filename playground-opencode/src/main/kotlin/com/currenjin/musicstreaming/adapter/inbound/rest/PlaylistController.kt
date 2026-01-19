package com.currenjin.musicstreaming.adapter.inbound.rest

import com.currenjin.musicstreaming.adapter.inbound.rest.dto.*
import com.currenjin.musicstreaming.domain.model.PlaylistId
import com.currenjin.musicstreaming.domain.model.TrackId
import com.currenjin.musicstreaming.domain.model.UserId
import com.currenjin.musicstreaming.domain.port.inbound.ManagePlaylistUseCase

class PlaylistController(
    private val managePlaylistUseCase: ManagePlaylistUseCase,
) {
    fun createPlaylist(request: CreatePlaylistRequest): PlaylistResponse {
        val playlist = managePlaylistUseCase.createPlaylist(
            userId = UserId(request.userId),
            name = request.name,
        )
        return PlaylistResponse.from(playlist)
    }

    fun getPlaylist(playlistId: String): PlaylistResponse? {
        val playlist = managePlaylistUseCase.getPlaylist(PlaylistId(playlistId))
        return playlist?.let { PlaylistResponse.from(it) }
    }

    fun getUserPlaylists(userId: String): List<PlaylistResponse> {
        val playlists = managePlaylistUseCase.getUserPlaylists(UserId(userId))
        return playlists.map { PlaylistResponse.from(it) }
    }

    fun addTrackToPlaylist(playlistId: String, request: AddTrackToPlaylistRequest): PlaylistResponse {
        val playlist = managePlaylistUseCase.addTrackToPlaylist(
            playlistId = PlaylistId(playlistId),
            trackId = TrackId(request.trackId),
            userId = UserId(request.userId),
        )
        return PlaylistResponse.from(playlist)
    }

    fun removeTrackFromPlaylist(playlistId: String, request: RemoveTrackFromPlaylistRequest): PlaylistResponse {
        val playlist = managePlaylistUseCase.removeTrackFromPlaylist(
            playlistId = PlaylistId(playlistId),
            trackId = TrackId(request.trackId),
            userId = UserId(request.userId),
        )
        return PlaylistResponse.from(playlist)
    }

    fun renamePlaylist(playlistId: String, request: RenamePlaylistRequest): PlaylistResponse {
        val playlist = managePlaylistUseCase.renamePlaylist(
            playlistId = PlaylistId(playlistId),
            newName = request.newName,
            userId = UserId(request.userId),
        )
        return PlaylistResponse.from(playlist)
    }

    fun deletePlaylist(playlistId: String, userId: String) {
        managePlaylistUseCase.deletePlaylist(
            playlistId = PlaylistId(playlistId),
            userId = UserId(userId),
        )
    }

    fun makePlaylistPublic(playlistId: String, userId: String): PlaylistResponse {
        val playlist = managePlaylistUseCase.makePlaylistPublic(
            playlistId = PlaylistId(playlistId),
            userId = UserId(userId),
        )
        return PlaylistResponse.from(playlist)
    }

    fun makePlaylistPrivate(playlistId: String, userId: String): PlaylistResponse {
        val playlist = managePlaylistUseCase.makePlaylistPrivate(
            playlistId = PlaylistId(playlistId),
            userId = UserId(userId),
        )
        return PlaylistResponse.from(playlist)
    }
}
