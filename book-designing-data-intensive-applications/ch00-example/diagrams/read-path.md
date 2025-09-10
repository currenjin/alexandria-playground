# Read Path with Timeouts/Retry (Text Diagram)

Client
└── HTTP GET /orders/{id} (t=500ms total)
└── API GW (t=100ms, retry=0)
└── OrderService (t=350ms budget)
├── Cache (GET key=order:{id}, t=50ms, timeout=60ms)
│     └── hit -> return
│
└── miss -> DB (SELECT ... JOIN ..., t=200ms, timeout=250ms, retries=0)
└── Cache SET (TTL=5m, stampede lock 100ms)
└── return

Notes:
- 각 화살표 라벨에 timeout/retry/idempotent 표기.
- Error budget은 GW/Service/Downstream에 분배.
