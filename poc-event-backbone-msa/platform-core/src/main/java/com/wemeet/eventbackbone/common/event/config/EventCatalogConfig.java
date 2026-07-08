package com.wemeet.eventbackbone.common.event.config;

import com.wemeet.eventbackbone.common.event.EventTypes;

import com.wemeet.eventbackbone.contracts.ContractCatalog;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * 기동 시 전체 계약을 EventTypes에 등록 (§7.1.2). 발행/소비 어느 쪽이든 type↔class 매핑이 필요하므로
 * 서비스마다 전체 카탈로그를 등록(무해·멱등). 핸들러 등록(HandlerRegistry)과는 별개.
 */
@Configuration
public class EventCatalogConfig {
    @PostConstruct
    void registerAll() {
        ContractCatalog.ALL.forEach(EventTypes::register);
    }
}
