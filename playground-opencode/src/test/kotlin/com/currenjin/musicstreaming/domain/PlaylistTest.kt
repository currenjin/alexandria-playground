package com.currenjin.musicstreaming.domain

import com.currenjin.musicstreaming.domain.model.Playlist
import com.currenjin.musicstreaming.domain.model.PlaylistId
import com.currenjin.musicstreaming.domain.model.TrackId
import com.currenjin.musicstreaming.domain.model.UserId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PlaylistTest {

    @Test
    fun new_playlist_is_empty() {
        val playlist = createPlaylist()

        assertTrue(playlist.isEmpty())
        assertEquals(0, playlist.trackCount())
    }

    @Test
    fun add_track_increases_track_count() {
        val playlist = createPlaylist()
        val trackId = TrackId("track-1")

        val updated = playlist.addTrack(trackId)

        assertEquals(1, updated.trackCount())
        assertFalse(updated.isEmpty())
    }

    @Test
    fun add_duplicate_track_does_not_increase_count() {
        val trackId = TrackId("track-1")
        val playlist = createPlaylist().addTrack(trackId)

        val updated = playlist.addTrack(trackId)

        assertEquals(1, updated.trackCount())
    }

    @Test
    fun remove_track_decreases_track_count() {
        val trackId = TrackId("track-1")
        val playlist = createPlaylist().addTrack(trackId)

        val updated = playlist.removeTrack(trackId)

        assertEquals(0, updated.trackCount())
        assertTrue(updated.isEmpty())
    }

    @Test
    fun reorder_tracks_changes_order() {
        val track1 = TrackId("track-1")
        val track2 = TrackId("track-2")
        val track3 = TrackId("track-3")
        val playlist = createPlaylist()
            .addTrack(track1)
            .addTrack(track2)
            .addTrack(track3)

        val newOrder = listOf(track3, track1, track2)
        val updated = playlist.reorderTracks(newOrder)

        assertEquals(newOrder, updated.trackIds)
    }

    @Test
    fun reorder_tracks_with_different_tracks_throws_exception() {
        val track1 = TrackId("track-1")
        val track2 = TrackId("track-2")
        val playlist = createPlaylist()
            .addTrack(track1)
            .addTrack(track2)

        val invalidOrder = listOf(track1, TrackId("track-3"))

        assertThrows(IllegalArgumentException::class.java) {
            playlist.reorderTracks(invalidOrder)
        }
    }

    @Test
    fun make_public_changes_visibility() {
        val playlist = createPlaylist()

        assertFalse(playlist.isPublic)

        val publicPlaylist = playlist.makePublic()

        assertTrue(publicPlaylist.isPublic)
    }

    @Test
    fun make_private_changes_visibility() {
        val playlist = createPlaylist().makePublic()

        val privatePlaylist = playlist.makePrivate()

        assertFalse(privatePlaylist.isPublic)
    }

    @Test
    fun rename_changes_name() {
        val playlist = createPlaylist()

        val renamed = playlist.rename("New Name")

        assertEquals("New Name", renamed.name)
    }

    private fun createPlaylist(): Playlist = Playlist(
        id = PlaylistId("playlist-1"),
        name = "My Playlist",
        ownerId = UserId("user-1"),
    )
}
