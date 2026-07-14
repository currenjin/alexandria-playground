package com.wemeet.eventbackbone.common.event.contract;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 이벤트 봉투·payload 전용 ObjectMapper.
 *
 * <p>일부러 Spring 빈이 아니다. 빈으로 두면(이름과 무관하게) Boot의 Jackson 자동설정이 물러나
 * 소비 앱의 REST 직렬화까지 바꿔버린다. 백본의 직렬화 규칙은 계약의 일부이므로 앱 설정과 분리해
 * 여기 고정한다.</p>
 *
 * <ul>
 *   <li>날짜는 ISO-8601 문자열(타임스탬프 금지) — 봉투 occurredAt 규격.</li>
 *   <li>미지 필드 관용(FAIL_ON_UNKNOWN_PROPERTIES off) — 이벤트에 필드가 추가돼도(호환 변경)
 *       구버전 컨슈머가 역직렬화 실패로 DLT에 빠지지 않게 한다.</li>
 * </ul>
 */
public final class EventJson {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private EventJson() {
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }
}
