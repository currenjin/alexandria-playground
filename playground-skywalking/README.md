# playground-skywalking

Apache SkyWalking 분산 추적 예제. 3개 서비스 간 HTTP 호출을 SkyWalking으로 관찰한다.

## 구조

```
Client → order-service (8080)
              ↓
       product-service (8081)  ← 랜덤 지연 (20% 확률)
              ↓
       payment-service (8082)  ← 10% 실패 시뮬레이션
```

SkyWalking UI는 http://localhost:8090 에서 확인한다.

## 실행

```bash
docker compose up --build
```

빌드 후 OAP가 완전히 뜨기까지 30~60초 걸린다.

## 요청 예시

```bash
# 정상 주문
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"productId": "product-1", "quantity": 2}'

# 재고 없음 (productId = "out-of-stock")
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"productId": "out-of-stock", "quantity": 1}'

# 여러 번 요청해서 latency, error 분포 확인
for i in {1..20}; do
  curl -s -X POST http://localhost:8080/orders \
    -H "Content-Type: application/json" \
    -d '{"productId": "product-1", "quantity": 1}' | jq .
done
```

## SkyWalking UI에서 볼 수 있는 것

- **Topology**: order → product, order → payment 서비스 의존성 그래프
- **Trace**: 개별 요청의 전체 흐름 (각 서비스에서 걸린 시간 포함)
- **Service**: 서비스별 응답 시간, throughput, error rate
- **Slow traces**: product-service 지연이 발생한 trace만 필터링

## 시나리오

| 상황 | 발생 조건 |
|------|----------|
| 정상 trace | 일반 요청 |
| Slow trace | product-service가 300~700ms 지연 (20% 확률) |
| Error trace | payment-service 500 응답 (10% 확률) |
| Out of stock | `productId: "out-of-stock"` 전달 |
