# ADR-001: Cache-Aside on GET /orders/{id}

## Context
- 읽기 급증 구간에서 `/orders/{id}` p95가 220ms까지 상승.
- DB는 주문 헤더+아이템 조인을 수행, 핫키가 존재. 최신성 요구는 “수 분 내”.
- 목표 SLO: p95 < 200ms, 에러율 < 0.1%, 캐시 hit-rate ≥ 80%.

## Decision
- **Cache-Aside + TTL 5m + 스탬피드 방지(분산락, 요청 합치기)**.
- 키: `order:{id}`. 실패 시 기본값/404 캐시(짧은 TTL)로 스캔 방지.

## Consequences
- (+) 애플리케이션이 무효화 통제, 구현 단순. 캐시 장애시 우회 용이.
- (-) miss 순간 지연 피크 가능. TTL과 신선도 균형 필요.

## Alternatives
- Read-through(프록시 계층), Write-through/Write-back(일관성↑, 복잡↑).

## Evidence
- k6 10k RPS, 히트율 85%에서 p95 **220→150ms**, 에러율 0.03%.
- 슬로키 상위 5%는 캐시 오히려 역효과 → 별도 핫키 예열/핀ning 필요.
