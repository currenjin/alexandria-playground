### 투두
- [x] 오더
- [x] 헤더 템플릿
    - [x] 생성
- [x] 조직 생성 서비스
- [x] 헤더 템플릿 조회 서비스
- [x] 조직 커스텀 헤더 조회 서비스
- [x] 정렬
- [x] 값 매칭
- [x] 노출 여부 매칭
- [x] 조직 커스텀 헤더
    - [x] 생성
    - [x] 실제 데이터와 매칭
    - [x] 순서대로 정렬
    - [x] isVisible 매칭
- [x] 유저 커스텀 헤더
    - [x] 생성
    - [x] 실제 데이터와 매칭
    - [x] 순서대로 정렬
    - [x] isVisible 매칭
    - [x] 조직이 우선순위가 높다

### 데이터 형태
```json
{
  "table": "orders",
  "columns": [
    { "key": "id",           "label": "ID",       "visible": true,  "sequence": 1 },
    { "key": "buyerId",      "label": "거래처ID", "visible": true,  "sequence": 2 },
    { "key": "buyerName",    "label": "거래처",   "visible": true,  "sequence": 3 },
    { "key": "receivedDate", "label": "접수일",   "visible": true,  "sequence": 4 },
    { "key": "orderCode",    "label": "주문코드", "visible": true,  "sequence": 5 },
    { "key": "orderNumber",  "label": "주문번호", "visible": true,  "sequence": 6 }
  ],
  "rows": [
    {
      "id": 1,
      "buyerId": 1,
      "buyerName": "거래처",
      "receivedDate": "2025-05-24",
      "orderCode": "R-123-123",
      "orderNumber": "O-123-123"
    },
    {
      "id": 2,
      "buyerId": 2,
      "buyerName": "테스트상사",
      "receivedDate": "2025-05-25",
      "orderCode": "R-124-124",
      "orderNumber": "O-124-124"
    }
  ]
}
```

### 고민
- [] 유저 커스터 헤더가 헤더 템플릿과
- [] 컬럼 추가시 표편집 한사람도 자동 추가되게 해주세요.