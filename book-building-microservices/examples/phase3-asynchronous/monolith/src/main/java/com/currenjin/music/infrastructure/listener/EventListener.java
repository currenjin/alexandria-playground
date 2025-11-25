package com.currenjin.music.infrastructure.listener;

import com.currenjin.music.share.DomainEvent;

public interface EventListener {
    void onEvent(DomainEvent event);
}
