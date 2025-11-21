package com.currenjin.music.client.user.dto;

import java.time.LocalDateTime;

public record UserDto(Long id, String email, String username, LocalDateTime createdAt) { }
