package com.wemeet.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * {@link EnableEventBackbone}이 import하는 설정. platform-core 공통 인프라
 * (com.wemeet.core)를 스캔해 백본 빈을 등록하고, 릴레이·사가 타임아웃 스캐너용
 * 스케줄링을 켠다. 앱은 자기 패키지만 스캔하고(@SpringBootApplication 기본), 백본은 이 import로만 켜진다.
 * (TopicCatalogProperties 바인딩은 스캔된 KafkaTopicConfig의 @EnableConfigurationProperties가 처리한다.)
 */
@Configuration
@ComponentScan("com.wemeet.core")
@EnableScheduling
public class EventBackboneConfig {
}
