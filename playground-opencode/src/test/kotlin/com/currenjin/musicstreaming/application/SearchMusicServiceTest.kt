package com.currenjin.musicstreaming.application

import com.currenjin.musicstreaming.adapter.outbound.persistence.InMemoryAlbumRepository
import com.currenjin.musicstreaming.adapter.outbound.persistence.InMemoryArtistRepository
import com.currenjin.musicstreaming.adapter.outbound.persistence.InMemoryTrackRepository
import com.currenjin.musicstreaming.domain.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate

class SearchMusicServiceTest {

    private lateinit var trackRepository: InMemoryTrackRepository
    private lateinit var albumRepository: InMemoryAlbumRepository
    private lateinit var artistRepository: InMemoryArtistRepository
    private lateinit var service: SearchMusicService

    private val artistId = ArtistId("artist-1")
    private val albumId = AlbumId("album-1")

    @BeforeEach
    fun setup() {
        trackRepository = InMemoryTrackRepository()
        albumRepository = InMemoryAlbumRepository()
        artistRepository = InMemoryArtistRepository()
        service = SearchMusicService(trackRepository, albumRepository, artistRepository)

        artistRepository.save(Artist(
            id = artistId,
            name = "The Beatles"
        ))

        albumRepository.save(Album(
            id = albumId,
            title = "Abbey Road",
            artistId = artistId,
            releaseDate = LocalDate.of(1969, 9, 26)
        ))

        trackRepository.save(Track(
            id = TrackId("track-1"),
            title = "Come Together",
            artistId = artistId,
            albumId = albumId,
            duration = Duration.ofMinutes(4),
            audioUrl = "http://example.com/come-together.mp3",
            genre = Genre.ROCK,
            playCount = 1000
        ))

        trackRepository.save(Track(
            id = TrackId("track-2"),
            title = "Something",
            artistId = artistId,
            albumId = albumId,
            duration = Duration.ofMinutes(3),
            audioUrl = "http://example.com/something.mp3",
            genre = Genre.ROCK,
            playCount = 800
        ))
    }

    @Test
    fun search_tracks_returns_matching_tracks() {
        val results = service.searchTracks("Come")

        assertEquals(1, results.size)
        assertEquals("Come Together", results[0].title)
    }

    @Test
    fun search_tracks_is_case_insensitive() {
        val results = service.searchTracks("come")

        assertEquals(1, results.size)
    }

    @Test
    fun search_albums_returns_matching_albums() {
        val results = service.searchAlbums("Abbey")

        assertEquals(1, results.size)
        assertEquals("Abbey Road", results[0].title)
    }

    @Test
    fun search_artists_returns_matching_artists() {
        val results = service.searchArtists("Beatles")

        assertEquals(1, results.size)
        assertEquals("The Beatles", results[0].name)
    }

    @Test
    fun search_all_returns_combined_results() {
        val results = service.searchAll("The")

        assertFalse(results.artists.isEmpty())
    }

    @Test
    fun get_tracks_by_genre_returns_tracks() {
        val results = service.getTracksByGenre(Genre.ROCK)

        assertEquals(2, results.size)
    }

    @Test
    fun get_top_tracks_returns_sorted_by_play_count() {
        val results = service.getTopTracks(10)

        assertEquals(2, results.size)
        assertEquals("Come Together", results[0].title)
        assertEquals("Something", results[1].title)
    }

    @Test
    fun get_artist_tracks_returns_artist_tracks() {
        val results = service.getArtistTracks(artistId)

        assertEquals(2, results.size)
    }

    @Test
    fun get_artist_tracks_throws_when_artist_not_found() {
        val unknownArtist = ArtistId("unknown")

        assertThrows(MusicStreamingError.ArtistNotFound::class.java) {
            service.getArtistTracks(unknownArtist)
        }
    }

    @Test
    fun get_album_tracks_returns_album_tracks() {
        val results = service.getAlbumTracks(albumId)

        assertEquals(2, results.size)
    }

    @Test
    fun get_album_tracks_throws_when_album_not_found() {
        val unknownAlbum = AlbumId("unknown")

        assertThrows(MusicStreamingError.AlbumNotFound::class.java) {
            service.getAlbumTracks(unknownAlbum)
        }
    }
}
