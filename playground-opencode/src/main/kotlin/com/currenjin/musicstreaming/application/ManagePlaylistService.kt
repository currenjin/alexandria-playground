package com.currenjin.musicstreaming.application

import com.currenjin.musicstreaming.domain.model.*
import com.currenjin.musicstreaming.domain.port.inbound.ManagePlaylistUseCase
import com.currenjin.musicstreaming.domain.port.outbound.PlaylistRepository
import com.currenjin.musicstreaming.domain.port.outbound.TrackRepository
import com.currenjin.musicstreaming.domain.port.outbound.UserRepository
import java.util.UUID

class ManagePlaylistService(
    private val playlistRepository: PlaylistRepository,
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository,
) : ManagePlaylistUseCase {

    override fun createPlaylist(userId: UserId, name: String): Playlist {
        userRepository.findById(userId)
            ?: throw MusicStreamingError.UserNotFound(userId)

        val playlist = Playlist(
            id = PlaylistId(UUID.randomUUID().toString()),
            name = name,
            ownerId = userId,
        )

        return playlistRepository.save(playlist)
    }

    override fun getPlaylist(playlistId: PlaylistId): Playlist? {
        return playlistRepository.findById(playlistId)
    }

    override fun getUserPlaylists(userId: UserId): List<Playlist> {
        userRepository.findById(userId)
            ?: throw MusicStreamingError.UserNotFound(userId)

        return playlistRepository.findByUserId(userId)
    }

    override fun addTrackToPlaylist(playlistId: PlaylistId, trackId: TrackId, userId: UserId): Playlist {
        val playlist = getPlaylistWithOwnerCheck(playlistId, userId)

        trackRepository.findById(trackId)
            ?: throw MusicStreamingError.TrackNotFound(trackId)

        return playlistRepository.save(playlist.addTrack(trackId))
    }

    override fun removeTrackFromPlaylist(playlistId: PlaylistId, trackId: TrackId, userId: UserId): Playlist {
        val playlist = getPlaylistWithOwnerCheck(playlistId, userId)
        return playlistRepository.save(playlist.removeTrack(trackId))
    }

    override fun renamePlaylist(playlistId: PlaylistId, newName: String, userId: UserId): Playlist {
        val playlist = getPlaylistWithOwnerCheck(playlistId, userId)
        return playlistRepository.save(playlist.rename(newName))
    }

    override fun deletePlaylist(playlistId: PlaylistId, userId: UserId) {
        getPlaylistWithOwnerCheck(playlistId, userId)
        playlistRepository.delete(playlistId)
    }

    override fun makePlaylistPublic(playlistId: PlaylistId, userId: UserId): Playlist {
        val playlist = getPlaylistWithOwnerCheck(playlistId, userId)
        return playlistRepository.save(playlist.makePublic())
    }

    override fun makePlaylistPrivate(playlistId: PlaylistId, userId: UserId): Playlist {
        val playlist = getPlaylistWithOwnerCheck(playlistId, userId)
        return playlistRepository.save(playlist.makePrivate())
    }

    private fun getPlaylistWithOwnerCheck(playlistId: PlaylistId, userId: UserId): Playlist {
        val playlist = playlistRepository.findById(playlistId)
            ?: throw MusicStreamingError.PlaylistNotFound(playlistId)

        if (playlist.ownerId != userId) {
            throw MusicStreamingError.UnauthorizedAccess("User ${userId.value} is not the owner of playlist ${playlistId.value}")
        }

        return playlist
    }
}
