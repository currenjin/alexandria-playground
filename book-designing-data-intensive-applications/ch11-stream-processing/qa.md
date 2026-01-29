### Q1. 이벤트 시간(Event Time) vs 처리 시간(Processing Time)은 언제 각각 쓰나요?
A. 이벤트 시간: 정확한 시간 기반 분석, 재처리 결과 일관성 필요 시. 처리 시간: 단순, 저지연, 모니터링/알람. 대부분 분석은 이벤트 시간, 실시간 대시보드는 처리 시간도 가능.

### Q2. 워터마크(Watermark)란 무엇인가요?
A. "이 시점 이전 이벤트는 (거의) 다 도착했다"는 추정. 윈도우 종료 시점 결정에 사용. 너무 보수적이면 지연↑, 너무 공격적이면 지연 이벤트↑. 소스별 지연 분포 기반 설정.

### Q3. 지연 이벤트(Late Event)는 어떻게 처리하나요?
A. 허용 지연 창(Allowed Lateness) 설정 → 창 내 도착하면 윈도우 재계산. 창 넘으면: 버림, 사이드 출력(별도 처리), 근사 집계. 정확도↔지연↔리소스 트레이드오프.

### Q4. Exactly-once 처리는 어떻게 달성하나요?
A. 시스템 레벨 exactly-once: Kafka Transactions + Flink 체크포인트. 또는 At-least-once + 소비자 멱등성(효과적 exactly-once). 외부 시스템 쓰기는 멱등 연산 또는 2PC 필요.

### Q5. 윈도우(Tumbling, Sliding, Session) 선택 기준은?
A. Tumbling: 고정 구간 집계(시간별 매출), 겹침 없음. Sliding: 이동 평균, 겹침 있음. Session: 사용자 활동 기반, 가변 크기. 비즈니스 요구에 맞게 선택. Session은 상태 관리 복잡.

### Q6. 스트림-테이블 이중성(Duality)이란?
A. 스트림 = 변경 로그(changelog), 테이블 = 스트림의 최신 스냅샷. 스트림을 재생하면 테이블 복원, 테이블 변경을 캡처하면 스트림(CDC). Kafka Streams의 KTable이 예시.

### Q7. Kafka Streams vs Flink는 어떻게 선택하나요?
A. Kafka Streams: 라이브러리(별도 클러스터 불필요), Kafka 전용, 단순. Flink: 독립 클러스터, 다양한 소스, 고급 기능(이벤트 시간, 정교한 윈도우). 소규모/Kafka 전용이면 KStreams, 복잡하면 Flink.

### Q8. 백프레셔(Backpressure)는 어떻게 처리하나요?
A. 소비자가 생산자보다 느릴 때 발생. 처리: 버퍼링(메모리/디스크), 드롭(샘플링), 생산자 속도 제한. Flink/Kafka Streams는 자동 백프레셔. 모니터링: 소비자 지연(lag), 버퍼 사용량.
