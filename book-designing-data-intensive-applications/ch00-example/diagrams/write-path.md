# Write Path with Idempotency (Text Diagram)

Client
└── POST /orders  (Idempotency-Key: <uuid>)
└── API GW
└── OrderService
├── Idempotency Store check (get/set by key, TTL=24h)
├── DB transaction (insert order, items)
└── Outbox append (event=OrderCreated)
└── CDC / Stream -> Downstreams (Inventory, Notification)

Notes:
- 재시도 시 동일 키로 중복 방지.
- Outbox+CDC로 at-least-once를 exactly-once-like로 보정(소비측 멱등).
