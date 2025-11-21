package com.currenjin.music.client.song.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.currenjin.music.client.song.SongClient;
import com.currenjin.music.client.song.dto.SongDto;

@Component
public class SongClientImpl implements SongClient {
    private RestTemplate restTemplate;

	public SongClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

    @Value("${target.song-service.url}")
    private String baseUrl;

	@Override
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
	public List<SongDto> findAllBySongIds(List<Long> songIds) {
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

		return response.getBody();
	}

	@Override
	public Optional<SongDto> findById(Long songId) {
		try {
			ResponseEntity<SongDto> response = restTemplate.getForEntity(
				baseUrl + "/api/songs/" + songId,
				SongDto.class);

			return Optional.ofNullable(response.getBody());
		} catch (HttpClientErrorException.NotFound e) {
			return Optional.empty();
		}
	}

}
