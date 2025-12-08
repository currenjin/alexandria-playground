package com.currenjin.music.share.user;

import java.time.LocalDateTime;

public record CachedUser(
        Long id,
        String email,
        String username,
        LocalDateTime createdAt
) {
    @Override
    public String toString() {
        return "CachedUser[" +
                "id=" + id + ", " +
                "email=" + email + ", " +
                "username=" + username + ", " +
                "createdAt=" + createdAt + ']';
    }
}
