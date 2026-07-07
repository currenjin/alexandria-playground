package com.wemeet.eventbackbone.contracts;

import com.wemeet.eventbackbone.contracts.OrderContracts.*;
import com.wemeet.eventbackbone.contracts.SettlementContracts.*;
import com.wemeet.eventbackbone.contracts.TripContracts.*;

import java.util.List;

/**
 * 전체 이벤트·커맨드 계약 목록. 각 서비스가 기동 시 EventTypes에 등록(type↔class 매핑).
 * 실제는 contracts 모듈 스캔 + CI 게이트로 관리 (§7.1.2 거버넌스).
 */
public final class ContractCatalog {
    private ContractCatalog() {}

    public static final List<Class<? extends DomainEvent>> ALL = List.of(
            OrderConfirmed.class, OrderCancelled.class, CancelOrder.class,
            CreateTrip.class, CancelTrip.class, TripDispatched.class, TripCreationFailed.class,
            ScheduleSettlement.class, SettlementScheduled.class
    );
}
