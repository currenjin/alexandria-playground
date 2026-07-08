package com.wemeet.eventbackbone.common.event.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 토픽 카탈로그 (§7.1.2) — 토픽 이름·파티션·리텐션을 <b>as-code(yaml)로 선언</b>, 공통 소유.
 * `classpath:event-topics.yml`(platform-core 제공)에서 바인딩되고, 모든 서비스가 같은 카탈로그를 임포트한다.
 * 운영 클러스터는 auto-create를 끄고(§7.1.2) 이 선언만으로 토픽이 생성된다.
 */
@ConfigurationProperties(prefix = "platform.events")
public class TopicCatalogProperties {

    private List<TopicSpec> topics = new ArrayList<>();

    public List<TopicSpec> getTopics() { return topics; }
    public void setTopics(List<TopicSpec> topics) { this.topics = topics; }

    public static class TopicSpec {
        private String name;
        private int partitions = 12;
        private long retentionMs = 604_800_000L;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getPartitions() { return partitions; }
        public void setPartitions(int partitions) { this.partitions = partitions; }
        public long getRetentionMs() { return retentionMs; }
        public void setRetentionMs(long retentionMs) { this.retentionMs = retentionMs; }
    }
}
