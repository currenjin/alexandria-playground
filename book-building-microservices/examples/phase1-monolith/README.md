# Phase 1: Music Streaming Monolith

Building Microservices 책의 개념을 실습하기 위한 음악 스트리밍 서비스 모놀리스 구현입니다.

## 개요

간단한 음악 스트리밍 서비스를 모놀리식 아키텍처로 구현했습니다.
- **모든 도메인이 하나의 애플리케이션에 포함**
- **단일 데이터베이스 (H2 인메모리)**
- **도메인 간 직접 참조 (UserRepository, SongRepository 등)**

## 도메인 구성

### 1. User (사용자 관리)
- 사용자 등록 및 조회
- 이메일 기반 중복 체크

### 2. Song (곡 관리)
- 곡 등록 및 조회
- 장르/아티스트별 검색

### 3. Playlist (재생목록 관리)
- 재생목록 생성
- 재생목록에 곡 추가/삭제
- 사용자별 재생목록 조회

### 4. Streaming (재생 기록)
- 재생 이력 기록
- 최근 재생 목록 조회
- 인기 곡 통계

## 실행 방법

```bash
# 빌드
./gradlew build

# 실행
./gradlew bootRun
```

애플리케이션이 `http://localhost:8080`에서 실행됩니다.

## API 테스트

### 1. 사용자 조회
```bash
# 모든 사용자 조회
curl http://localhost:8080/api/users

# 특정 사용자 조회
curl http://localhost:8080/api/users/1

# 사용자 생성
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"email": "test@music.com", "username": "testuser"}'
```

### 2. 곡 조회
```bash
# 모든 곡 조회
curl http://localhost:8080/api/songs

# 특정 곡 조회
curl http://localhost:8080/api/songs/1

# 장르별 조회
curl "http://localhost:8080/api/songs?genre=Rock"

# 아티스트별 조회
curl "http://localhost:8080/api/songs?artist=Queen"
```

### 3. 재생목록 관리
```bash
# 재생목록 생성
curl -X POST http://localhost:8080/api/playlists \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "name": "My Favorites"}'

# 재생목록 조회
curl http://localhost:8080/api/playlists/1

# 사용자별 재생목록 조회
curl "http://localhost:8080/api/playlists?userId=1"

# 재생목록에 곡 추가
curl -X POST http://localhost:8080/api/playlists/1/songs/5

# 재생목록의 곡 목록 조회
curl http://localhost:8080/api/playlists/1/songs

# 재생목록에서 곡 삭제
curl -X DELETE http://localhost:8080/api/playlists/1/songs/5
```

### 4. 스트리밍 (재생 기록)
```bash
# 곡 재생 기록
curl -X POST http://localhost:8080/api/streaming/play \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "songId": 1}'

# 사용자 재생 이력 조회 (최근 30일)
curl "http://localhost:8080/api/streaming/history?userId=1"

# 사용자 재생 이력 조회 (최근 7일)
curl "http://localhost:8080/api/streaming/history?userId=1&days=7"

# 전체 인기 곡 Top 10
curl "http://localhost:8080/api/streaming/statistics/most-played?limit=10"

# 특정 사용자의 인기 곡 Top 5
curl "http://localhost:8080/api/streaming/statistics/most-played/user/1?limit=5"
```

## H2 콘솔 접속

데이터베이스를 직접 확인하려면:
1. 브라우저에서 `http://localhost:8080/h2-console` 접속
2. JDBC URL: `jdbc:h2:mem:musicdb`
3. Username: `sa`
4. Password: (빈칸)

## 초기 데이터

애플리케이션 실행 시 자동으로 다음 데이터가 로드됩니다:
- 사용자 3명 (alice, bob, charlie)
- 곡 10개 (Queen, John Lennon, Michael Jackson 등)

## 모놀리스의 특징 (주목할 점)

### 1. 도메인 간 직접 참조
```java
// PlaylistService.java
private final UserRepository userRepository;  // 직접 참조
private final SongRepository songRepository;  // 직접 참조

public Playlist create(Long userId, String name) {
    // User 존재 확인 (같은 DB, 같은 트랜잭션)
    userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return playlistRepository.save(new Playlist(userId, name));
}
```

### 2. 단일 데이터베이스
- 모든 테이블이 하나의 데이터베이스에 존재
- JOIN 쿼리 자유롭게 사용 가능
- 트랜잭션 관리가 단순함

### 3. 간단한 배포
- 하나의 JAR 파일로 패키징
- 단일 프로세스로 실행
- 개발/테스트 환경 구성이 쉬움

## 다음 단계: Phase 2

Phase 2에서는 이 모놀리스를 점진적으로 마이크로서비스로 분해할 예정입니다:
- **교살자 무화과 패턴 (Strangler Fig Pattern)** 적용
- **User Service 분리**
- **API Gateway 도입**
- **Feature Toggle로 전환 제어**
