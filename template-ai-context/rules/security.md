# 보안 가이드

## 필수 보안 체크리스트

커밋 전 반드시 확인:

- [ ] 하드코딩된 시크릿 없음 (API 키, 비밀번호, 토큰)
- [ ] 모든 사용자 입력 검증됨
- [ ] SQL Injection 방지 (파라미터화된 쿼리 사용)
- [ ] XSS 방지 (HTML 새니타이제이션)
- [ ] CSRF 보호 활성화
- [ ] 인증/인가 검증 완료
- [ ] 모든 엔드포인트에 Rate Limiting 적용
- [ ] 에러 메시지에 민감 정보 노출 없음

## 시크릿 관리

### 하드코딩 금지

시크릿, API 키, 비밀번호 등을 코드에 직접 작성하지 않는다.

```kotlin
// Bad
val apiKey = "sk-1234567890abcdef"
val dbPassword = "mypassword123"

// Good
val apiKey = System.getenv("API_KEY")
    ?: throw IllegalStateException("API_KEY 환경변수가 설정되지 않았습니다")
val dbPassword = applicationProperties.database.password
```

### 환경 변수 사용

민감한 정보는 환경 변수 또는 설정 파일로 관리한다.

```yaml
# application.yml
database:
  password: ${DB_PASSWORD}

external:
  api-key: ${EXTERNAL_API_KEY}
```

### 커밋 전 확인사항

- `.env` 파일이 `.gitignore`에 포함되어 있는지 확인
- 시크릿이 포함된 파일이 커밋되지 않도록 주의
- `credentials.json`, `secrets.yaml` 등 민감한 파일 커밋 금지

## 금지 패턴

| 패턴 | 설명 |
|------|------|
| `password = "..."` | 비밀번호 하드코딩 |
| `apiKey = "..."` | API 키 하드코딩 |
| `secret = "..."` | 시크릿 하드코딩 |
| `token = "..."` | 토큰 하드코딩 |

## 입력 검증

### Controller 레벨 검증

```kotlin
@PostMapping("/orders")
fun createOrder(@Valid @RequestBody request: OrderRequest): OrderResponse {
    // @Valid로 기본 검증
    return orderFacade.createOrder(request)
}

data class OrderRequest(
    @field:NotNull
    val productId: Long,

    @field:Min(1)
    @field:Max(1000)
    val quantity: Int,

    @field:Size(max = 500)
    val note: String?
)
```

### SQL Injection 방지

```kotlin
// Bad - 문자열 연결
fun findByName(name: String): List<User> {
    val query = "SELECT * FROM users WHERE name = '$name'"
    return jdbcTemplate.query(query, userRowMapper)
}

// Good - 파라미터 바인딩
fun findByName(name: String): List<User> {
    val query = "SELECT * FROM users WHERE name = :name"
    return namedParameterJdbcTemplate.query(
        query,
        mapOf("name" to name),
        userRowMapper
    )
}

// Good - JPA Query Method
fun findByName(name: String): List<User>
```

## 보안 문제 대응 프로토콜

보안 문제 발견 시:

1. **즉시 중단** - 현재 작업 멈춤
2. **security-reviewer 에이전트 사용** - 보안 분석 수행
3. **CRITICAL 문제 우선 해결** - 심각도 높은 것부터
4. **노출된 시크릿 교체** - 즉시 rotation
5. **전체 코드베이스 검토** - 유사한 문제 확인

## 권장 사항

1. **Spring Cloud Config** 또는 **AWS Secrets Manager** 사용
2. **프로파일별 설정 분리**: `application-prod.yml`, `application-dev.yml`
3. **민감 정보 로깅 금지**: 로그에 비밀번호, 토큰 출력하지 않음
4. **HTTPS 강제**: 프로덕션 환경에서 HTTP 차단
5. **보안 헤더 설정**: X-Content-Type-Options, X-Frame-Options 등
