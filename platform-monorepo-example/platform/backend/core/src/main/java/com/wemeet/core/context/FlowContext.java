package com.wemeet.core.context;

import java.util.UUID;

/**
 * 실행 컨텍스트 — correlationId·causationId·tenant를 흐름 내내 운반.
 * 봉투를 잇는 것은 record가 아니라 이 컨텍스트다: 소비 리스너가 열고, 발행자가 읽어 봉투에 복사.
 * 예제라 ThreadLocal (확정 설계 문구는 ScopedValue/ThreadLocal 둘 다 허용).
 */
public final class FlowContext {
    public record Ctx(String tenantId, String corpId, String correlationId, UUID currentEventId) {}

    private static final ThreadLocal<Ctx> HOLDER = new ThreadLocal<>();

    private FlowContext() {}

    public static void open(String tenantId, String corpId, String correlationId, UUID currentEventId) {
        HOLDER.set(new Ctx(tenantId, corpId, correlationId, currentEventId));
    }
    public static Ctx get() { return HOLDER.get(); }
    public static void clear() { HOLDER.remove(); }

    /** HTTP/배치 진입점: correlationId 있으면 전파, 없으면 생성. currentEventId=없음(최초). */
    public static void openEntry(String tenantId, String corpId, String correlationId) {
        open(tenantId, corpId,
             correlationId != null ? correlationId : UUID.randomUUID().toString(),
             null);
    }
}
