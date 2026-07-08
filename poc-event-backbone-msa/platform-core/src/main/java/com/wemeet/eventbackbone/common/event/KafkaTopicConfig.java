package com.wemeet.eventbackbone.common.event;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * 토픽 생성 배선 (§7.1.2) — 카탈로그(as-code)를 읽어 파티션·리텐션과 함께 토픽을 선언.
 * Spring KafkaAdmin이 기동 시 멱등하게 생성한다(있으면 스킵). 운영 auto-create=off 전제.
 * 개발자는 토픽을 지정하지 않는다 — eventType만 선언하면 릴레이가 앞 두 마디로 토픽을 유도(§7.1.2).
 */
@Configuration
@EnableConfigurationProperties(TopicCatalogProperties.class)
public class KafkaTopicConfig {

    @Bean
    public KafkaAdmin.NewTopics eventTopics(TopicCatalogProperties props) {
        NewTopic[] topics = props.getTopics().stream()
                .map(t -> TopicBuilder.name(t.getName())
                        .partitions(t.getPartitions())
                        .replicas(1)   // 예제 단일 브로커. 실제 3+.
                        .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(t.getRetentionMs()))
                        .build())
                .toArray(NewTopic[]::new);
        return new KafkaAdmin.NewTopics(topics);
    }
}
