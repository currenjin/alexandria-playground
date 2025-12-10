package com.currenjin.music.infrastructure.client.song.impl;

import com.currenjin.music.infrastructure.client.song.SongClient;
import com.currenjin.music.infrastructure.client.song.dto.SongDto;
import com.currenjin.music.share.song.CachedSong;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SongClientImpl implements SongClient {
    private RestTemplate restTemplate;

	public SongClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

    @Value("${target.song-service.url}")
    private String baseUrl;

	@Override
	@Cacheable(cacheNames = "songExistsCache", key = "#songId")
    public Boolean songExists(Long songId) {
        try {
            ResponseEntity<SongDto> response = restTemplate.getForEntity(
                    baseUrl + "/api/songs/" + songId,
                    SongDto.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

	@Override
	@Cacheable(
		cacheNames = "songCache",
		key = "#songIds.stream().sorted().map(T(String).valueOf).collect(T(java.util.stream.Collectors).joining(','))"
	)
	public List<CachedSong> findAllBySongIds(List<Long> songIds) {
		String url = baseUrl + "/api/songs?ids=" + songIds.stream()
			.map(String::valueOf)
			.collect(Collectors.joining(","));

		ResponseEntity<List<SongDto>> response = restTemplate.exchange(
			url,
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<>() {
			}
		);
		List<SongDto> body = response.getBody();

        return body.stream().map(song ->
                new CachedSong(
                    song.id(),
                    song.title(),
                    song.artist(),
                    song.durationSeconds(),
                    song.genre()
                )
        ).toList();
	}

	@Override
	public Optional<CachedSong> findById(Long songId) {
		try {
			ResponseEntity<SongDto> response = restTemplate.getForEntity(
				baseUrl + "/api/songs/" + songId,
				SongDto.class);
			SongDto song = response.getBody();

			return Optional.of(new CachedSong(
					song.id(),
					song.title(),
					song.artist(),
					song.durationSeconds(),
					song.genre()
			));
		} catch (HttpClientErrorException.NotFound e) {
			return Optional.empty();
		}
	}

}
