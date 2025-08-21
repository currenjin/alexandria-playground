- [x] 오더
- [x] 헤더 템플릿
    - [x] 생성
- [] 유저 커스텀 헤더
    - [] 생성
    - [] 실제 데이터와 매칭
    - [] isVisible 매칭
- [x] 조직 생성 서비스
- [x] 헤더 템플릿 조회 서비스
- [x] 조직 커스텀 헤더 조회 서비스
- [] 정렬
- [] 값 매칭
- [] 노출 여부 매칭
- [] 조직 커스텀 헤더
    - [x] 생성
    - [] 가입 시 템플릿 기준 생성
    - [] 실제 데이터와 매칭
    - [] 순서대로 정렬
    - [] isVisible 매칭

데이터 형태
```json
{
  "id": {
    "value": 1,
    "isVisible": true
  },
  "buyerId": {
    "value": 1,
    "isVisible": true
  },
  "buyerName": {
    "value":   "거래처",
    "isVisible":  true
  },
  "receivedDate": {
    "value":  "2025-05-24",
    "isVisible":  true
  },
  "orderCode": {
    "value":   "R-123-123",
    "isVisible":  true
  },
  "orderNumber": {
    "value":  "O-123-123",
    "isVisible":  true
  }
}
```

고민
- [] 유저 커스터 헤더가 헤더 템플릿과
- [] 컬럼 추가시 표편집 한사람도 자동 추가되게 해주세요.