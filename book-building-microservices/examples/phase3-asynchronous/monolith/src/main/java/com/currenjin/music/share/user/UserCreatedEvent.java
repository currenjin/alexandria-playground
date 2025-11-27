package com.currenjin.music.share.user;

import com.currenjin.music.share.DomainEvent;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class UserCreatedEvent implements DomainEvent {
    String type;
    LocalDateTime occurredAt;

    Long id;
    String email;
    String username;
    LocalDateTime createdAt;

    public UserCreatedEvent(Long id, String email, String username, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.createdAt = createdAt;
        this.type = this.getClass().getSimpleName();
        this.occurredAt = LocalDateTime.now();
    }
}
