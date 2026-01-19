package com.currenjin.musicstreaming.application

import com.currenjin.musicstreaming.adapter.outbound.persistence.InMemoryPlaylistRepository
import com.currenjin.musicstreaming.adapter.outbound.persistence.InMemoryTrackRepository
import com.currenjin.musicstreaming.adapter.outbound.persistence.InMemoryUserRepository
import com.currenjin.musicstreaming.domain.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration

class ManagePlaylistServiceTest {

    private lateinit var playlistRepository: InMemoryPlaylistRepository
    private lateinit var trackRepository: InMemoryTrackRepository
    private lateinit var userRepository: InMemoryUserRepository
    private lateinit var service: ManagePlaylistService

    private val userId = UserId("user-1")
    private val trackId = TrackId("track-1")

    @BeforeEach
    fun setup() {
        playlistRepository = InMemoryPlaylistRepository()
        trackRepository = InMemoryTrackRepository()
        userRepository = InMemoryUserRepository()
        service = ManagePlaylistService(playlistRepository, trackRepository, userRepository)

        userRepository.save(User(
            id = userId,
            email = "test@example.com",
            displayName = "Test User"
        ))

        trackRepository.save(Track(
            id = trackId,
            title = "Test Track",
            artistId = ArtistId("artist-1"),
            albumId = AlbumId("album-1"),
            duration = Duration.ofMinutes(3),
            audioUrl = "http://example.com/track.mp3"
        ))
    }

    @Test
    fun create_playlist_returns_new_playlist() {
        val playlist = service.createPlaylist(userId, "My Playlist")

        assertNotNull(playlist)
        assertEquals("My Playlist", playlist.name)
        assertEquals(userId, playlist.ownerId)
        assertTrue(playlist.isEmpty())
    }

    @Test
    fun create_playlist_throws_when_user_not_found() {
        val unknownUser = UserId("unknown")

        assertThrows(MusicStreamingError.UserNotFound::class.java) {
            service.createPlaylist(unknownUser, "My Playlist")
        }
    }

    @Test
    fun get_playlist_returns_existing_playlist() {
        val created = service.createPlaylist(userId, "My Playlist")

        val found = service.getPlaylist(created.id)

        assertNotNull(found)
        assertEquals(created.id, found!!.id)
    }

    @Test
    fun get_playlist_returns_null_for_unknown_id() {
        val found = service.getPlaylist(PlaylistId("unknown"))

        assertNull(found)
    }

    @Test
    fun get_user_playlists_returns_all_user_playlists() {
        service.createPlaylist(userId, "Playlist 1")
        service.createPlaylist(userId, "Playlist 2")

        val playlists = service.getUserPlaylists(userId)

        assertEquals(2, playlists.size)
    }

    @Test
    fun add_track_to_playlist_adds_track() {
        val playlist = service.createPlaylist(userId, "My Playlist")

        val updated = service.addTrackToPlaylist(playlist.id, trackId, userId)

        assertEquals(1, updated.trackCount())
        assertTrue(updated.trackIds.contains(trackId))
    }

    @Test
    fun add_track_to_playlist_throws_when_not_owner() {
        val playlist = service.createPlaylist(userId, "My Playlist")
        val otherUser = UserId("other-user")
        userRepository.save(User(
            id = otherUser,
            email = "other@example.com",
            displayName = "Other User"
        ))

        assertThrows(MusicStreamingError.UnauthorizedAccess::class.java) {
            service.addTrackToPlaylist(playlist.id, trackId, otherUser)
        }
    }

    @Test
    fun add_track_to_playlist_throws_when_track_not_found() {
        val playlist = service.createPlaylist(userId, "My Playlist")
        val unknownTrack = TrackId("unknown")

        assertThrows(MusicStreamingError.TrackNotFound::class.java) {
            service.addTrackToPlaylist(playlist.id, unknownTrack, userId)
        }
    }

    @Test
    fun remove_track_from_playlist_removes_track() {
        val playlist = service.createPlaylist(userId, "My Playlist")
        service.addTrackToPlaylist(playlist.id, trackId, userId)

        val updated = service.removeTrackFromPlaylist(playlist.id, trackId, userId)

        assertEquals(0, updated.trackCount())
    }

    @Test
    fun rename_playlist_changes_name() {
        val playlist = service.createPlaylist(userId, "My Playlist")

        val renamed = service.renamePlaylist(playlist.id, "New Name", userId)

        assertEquals("New Name", renamed.name)
    }

    @Test
    fun delete_playlist_removes_playlist() {
        val playlist = service.createPlaylist(userId, "My Playlist")

        service.deletePlaylist(playlist.id, userId)

        assertNull(service.getPlaylist(playlist.id))
    }

    @Test
    fun make_playlist_public_changes_visibility() {
        val playlist = service.createPlaylist(userId, "My Playlist")

        val publicPlaylist = service.makePlaylistPublic(playlist.id, userId)

        assertTrue(publicPlaylist.isPublic)
    }

    @Test
    fun make_playlist_private_changes_visibility() {
        val playlist = service.createPlaylist(userId, "My Playlist")
        service.makePlaylistPublic(playlist.id, userId)

        val privatePlaylist = service.makePlaylistPrivate(playlist.id, userId)

        assertFalse(privatePlaylist.isPublic)
    }
}
