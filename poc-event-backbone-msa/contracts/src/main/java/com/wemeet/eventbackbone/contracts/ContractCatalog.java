package com.wemeet.eventbackbone.contracts;

import com.wemeet.eventbackbone.contracts.OrderContracts.*;
import com.wemeet.eventbackbone.contracts.SettlementContracts.*;
import com.wemeet.eventbackbone.contracts.DispatchContracts.*;

import java.util.List;

/**
 * 전체 이벤트·커맨드 계약 목록. 각 서비스가 기동 시 EventTypes에 등록(type↔class 매핑).
 * 실제는 contracts 모듈 스캔 + CI 게이트로 관리 (§7.1.2 거버넌스).
 */
public final class ContractCatalog {
    private ContractCatalog() {}

    public static final List<Class<? extends DomainEvent>> ALL = List.of(
            // OMS — 이벤트 + orchestrator→OMS 상태 동기화 커맨드
            OrderCreated.class, OrderCancelled.class, OrderCancelRejected.class,
            MarkDispatched.class, MarkDelivered.class, MarkSettled.class, MarkUndispatched.class,
            // TMS — 사실 이벤트 (커맨드 없음: 자기 API로 행위)
            DispatchCreated.class, DispatchDelivered.class, DispatchCancelled.class,
            // BMS — 이벤트 + orchestrator→BMS 정산 생성 커맨드
            SettlementCompleted.class, CreateSettlement.class
    );
}
