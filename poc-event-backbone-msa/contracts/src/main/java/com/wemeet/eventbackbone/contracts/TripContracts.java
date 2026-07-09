package com.wemeet.eventbackbone.contracts;

/**
 * 공유 contracts — TMS 소유. 배차 = 오더에 차량/기사 배정 후 운송(상차→하차).
 * TMS는 자기 API(배차·배송완료·배차취소)로 행위하고 결과 <b>사실(tms.trip.*)</b>만 발행한다(커맨드 소비 없음).
 * orchestrator가 이 사실을 받아 오더 상태 동기화·정산 트리거를 코디네이션한다.
 */
public final class TripContracts {
    private TripContracts() {}

    // ── 이벤트(사실) tms.trip.* ──
    @EventContract(type = "tms.trip.dispatched", version = 1)
    public record TripDispatched(String tripId, String orderId, String carrierId) implements DomainEvent {
        @Override public String aggregateId() { return tripId; }
    }

    @EventContract(type = "tms.trip.delivered", version = 1)
    public record TripDelivered(String tripId, String orderId) implements DomainEvent {
        @Override public String aggregateId() { return tripId; }
    }

    @EventContract(type = "tms.trip.cancelled", version = 1)
    public record TripCancelled(String tripId, String orderId) implements DomainEvent {
        @Override public String aggregateId() { return tripId; }
    }
}
