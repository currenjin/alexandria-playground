package com.wemeet.contract;

import com.wemeet.core.event.contract.DomainEvent;

import com.wemeet.contract.OrderContracts.*;
import com.wemeet.contract.SettlementContracts.*;
import com.wemeet.contract.DispatchContracts.*;

import java.util.List;

/**
 * 전체 이벤트·커맨드 계약 목록. 각 서비스가 기동 시 EventTypes에 등록(type↔class 매핑).
 * 실제는 contracts 모듈 스캔 + CI 게이트로 관리 (거버넌스).
 */
public final class ContractCatalog {
    private ContractCatalog() {}

    public static final List<Class<? extends DomainEvent>> ALL = List.of(
            // OMS — 오더 사실(권위) + 전이 시도 커맨드
            OrderCreated.class, OrderDispatched.class, OrderUndispatched.class, OrderDelivered.class,
            OrderSettled.class, OrderCancelled.class, OrderDispatchRejected.class, OrderCancelRejected.class,
            DispatchOrder.class, UndispatchOrder.class, DeliverOrder.class, SettleOrder.class,
            // TMS — 배차 사실 + 보상 커맨드
            DispatchCreated.class, DispatchDelivered.class, DispatchCancelled.class, CancelDispatch.class,
            // BMS — 정산 사실 + 커맨드
            SettlementCompleted.class, CreateSettlement.class
    );
}
