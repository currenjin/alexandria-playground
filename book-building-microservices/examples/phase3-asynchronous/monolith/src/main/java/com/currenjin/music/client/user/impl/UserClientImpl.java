package com.currenjin.music.client.user.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.currenjin.music.client.user.UserClient;
import com.currenjin.music.client.user.dto.UserDto;

@Component
public class UserClientImpl implements UserClient {
    private RestTemplate restTemplate;

	public UserClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

    @Value("${target.user-service.url}")
    private String baseUrl;

	@Override
    public Boolean userExists(Long userId) {
        try {
            ResponseEntity<UserDto> response = restTemplate.getForEntity(
                    baseUrl + "/api/users/" + userId,
                    UserDto.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
