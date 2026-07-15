package com.wemeet.core.event.contract;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 이벤트 전용 ObjectMapper의 계약(직렬화 규칙): 날짜=ISO 문자열, 미지 필드 관용.
 * 이 규칙은 봉투 규격의 일부라 앱 설정과 분리해 여기 고정된다.
 */
class EventJsonTest {

    private final ObjectMapper mapper = EventJson.mapper();

    @Test
    void 같은_싱글턴_mapper를_돌려준다() {
        assertThat(EventJson.mapper()).isSameAs(mapper);
    }

    @Test
    void 날짜는_ISO8601_문자열로_직렬화된다_타임스탬프_금지() throws Exception {
        OffsetDateTime t = OffsetDateTime.of(2026, 7, 15, 10, 30, 0, 0, ZoneOffset.UTC);

        String json = mapper.writeValueAsString(t);

        // 숫자 타임스탬프가 아니라 ISO 문자열이어야 한다.
        assertThat(json).startsWith("\"").contains("2026-07-15T10:30");
    }

    @Test
    void 미지_필드가_있어도_역직렬화_실패하지_않는다_호환변경_흡수() throws Exception {
        // 신버전이 추가한 필드(extraField)를 구버전 컨슈머가 만나도 DLT로 빠지면 안 된다.
        String json = "{\"orderId\":\"ORD-1\",\"extraField\":\"신버전이_추가한_필드\"}";

        Known known = mapper.readValue(json, Known.class);

        assertThat(known.orderId).isEqualTo("ORD-1");
    }

    @Test
    void treeToValue_왕복이_보존된다() throws Exception {
        JsonNode node = mapper.valueToTree(new Known("ORD-9"));

        Known back = mapper.treeToValue(node, Known.class);

        assertThat(back.orderId).isEqualTo("ORD-9");
    }

    static class Known {
        public String orderId;
        Known() {}
        Known(String orderId) { this.orderId = orderId; }
    }
}
