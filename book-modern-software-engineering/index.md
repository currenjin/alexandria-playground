# Modern Software Engineering 스터디 노트

> David Farley의 "Modern Software Engineering(모든 소프트웨어 엔지니어링)"을 정리한 노트입니다.
> 소프트웨어 개발을 진정한 엔지니어링 분야로 만들기 위한 원칙과 실천법을 다룹니다.

## Part I. What Is Software Engineering?

### [Ch01. Introduction](./ch01-introduction/map.md)
- **핵심**: 소프트웨어 개발은 공예가 아닌 엔지니어링이어야 한다. 과학적 사고와 체계적 접근이 필요하다.
- **키워드**: 소프트웨어 엔지니어링, 공예 vs 엔지니어링, 학습과 탐색

### [Ch02. What Is Engineering?](./ch02-what-is-engineering/map.md)
- **핵심**: 엔지니어링은 과학적 방법의 실용적 적용이다. 탐색과 학습을 통해 복잡한 문제를 해결한다.
- **키워드**: 과학적 방법, 실용주의, 합리적 의사결정

### [Ch03. Fundamentals of an Engineering Approach](./ch03-fundamentals-of-an-engineering-approach/map.md)
- **핵심**: 학습 최적화와 복잡성 관리가 소프트웨어 엔지니어링의 두 축이다.
- **키워드**: 학습 최적화, 복잡성 관리, 반복, 피드백, 점진주의, 실험주의, 경험주의

## Part II. Optimize for Learning

### [Ch04. Working Iteratively](./ch04-working-iteratively/map.md)
- **핵심**: 반복적 작업은 빠른 피드백과 방향 수정을 가능하게 한다. 큰 배치보다 작은 단계가 낫다.
- **키워드**: 반복, 작은 단계, 빅뱅 vs 점진적 접근, 실패 비용 최소화

### [Ch05. Feedback](./ch05-feedback/map.md)
- **핵심**: 피드백은 학습의 핵심 메커니즘이다. 피드백 루프를 짧고 빠르게 유지해야 한다.
- **키워드**: 피드백 루프, TDD, CI/CD, A/B 테스트, 배포 파이프라인

### [Ch06. Incrementalism](./ch06-incrementalism/map.md)
- **핵심**: 점진적으로 작은 변경을 누적하여 큰 변화를 만든다. 한 번에 큰 변경은 위험하다.
- **키워드**: 점진주의, 모듈식 설계, 릴리즈 가능한 단위, 브랜치 전략

### [Ch07. Empiricism](./ch07-empiricism/map.md)
- **핵심**: 가정이 아닌 증거에 기반하여 의사결정한다. 측정하고 관찰한 데이터로 판단한다.
- **키워드**: 경험주의, 데이터 기반 의사결정, 가설 검증, 편견 극복

### [Ch08. Being Experimental](./ch08-being-experimental/map.md)
- **핵심**: 실험적 접근은 가설을 세우고 검증하는 과학적 방법이다. 실패를 학습 기회로 활용한다.
- **키워드**: 실험, 가설, 피드백, 안전한 실패, 탐색적 테스트

## Part III. Optimize for Managing Complexity

### [Ch09. Modularity](./ch09-modularity/map.md)
- **핵심**: 모듈화는 복잡성을 다루는 가장 기본적인 전략이다. 독립적으로 변경 가능한 단위로 나눈다.
- **키워드**: 모듈, 경계, 인터페이스, 교체 가능성, 마이크로서비스

### [Ch10. Cohesion](./ch10-cohesion/map.md)
- **핵심**: 응집도가 높은 모듈은 하나의 명확한 책임을 가진다. 관련 있는 것들을 함께 둔다.
- **키워드**: 응집도, SRP, 기능적 응집, 우연적 응집, 패키지 구조

### [Ch11. Separation of Concerns](./ch11-separation-of-concerns/map.md)
- **핵심**: 관심사를 분리하면 각 부분을 독립적으로 이해하고 변경할 수 있다.
- **키워드**: 관심사 분리, 계층 구조, 포트와 어댑터, DDD, 수직 슬라이스

### [Ch12. Information Hiding and Abstraction](./ch12-information-hiding-and-abstraction/map.md)
- **핵심**: 구현 세부사항을 숨기고 안정적 인터페이스를 노출하면 변경 영향을 최소화한다.
- **키워드**: 정보 은닉, 추상화, 캡슐화, 인터페이스 설계, 누출된 추상화

### [Ch13. Managing Coupling](./ch13-managing-coupling/map.md)
- **핵심**: 결합도를 낮추면 한 부분의 변경이 다른 부분에 미치는 영향을 줄인다.
- **키워드**: 결합도, 느슨한 결합, DIP, 이벤트 기반, 비동기 통신

## Part IV. Tools to Support Engineering

### [Ch14. Testability](./ch14-testability/map.md)
- **핵심**: 테스트 가능한 설계는 좋은 설계다. 테스트가 설계를 이끌게 한다.
- **키워드**: TDD, 테스트 피라미드, 테스트 더블, 테스트 가능한 설계, 포트와 어댑터

### [Ch15. Deployability](./ch15-deployability/map.md)
- **핵심**: 배포는 일상적이고 지루한 이벤트여야 한다. 자동화된 파이프라인으로 위험을 낮춘다.
- **키워드**: CI/CD, 배포 파이프라인, 피처 플래그, 카나리 릴리즈, 블루/그린 배포

### [Ch16. Speed](./ch16-speed/map.md)
- **핵심**: 빠른 개발 속도는 품질을 희생하지 않고 얻어야 한다. 품질이 속도를 만든다.
- **키워드**: 개발 속도, 리드 타임, 처리량, DORA 메트릭, 지속 가능한 속도

### [Ch17. Controlling Variables](./ch17-controlling-variables/map.md)
- **핵심**: 변수를 통제해야 실험 결과를 신뢰할 수 있다. 환경, 데이터, 코드 변경을 격리한다.
- **키워드**: 변수 통제, 환경 일관성, 인프라 코드화, 컨테이너, 불변 인프라

---

## Quick Reference

### 소프트웨어 엔지니어링의 두 축
```
학습 최적화 (Optimize for Learning)
  ├── 반복 (Iteration)
  ├── 피드백 (Feedback)
  ├── 점진주의 (Incrementalism)
  ├── 경험주의 (Empiricism)
  └── 실험주의 (Being Experimental)

복잡성 관리 (Manage Complexity)
  ├── 모듈성 (Modularity)
  ├── 응집도 (Cohesion)
  ├── 관심사 분리 (Separation of Concerns)
  ├── 정보 은닉과 추상화 (Information Hiding & Abstraction)
  └── 결합도 관리 (Managing Coupling)
```

### DORA 메트릭
| 메트릭 | 설명 | 엘리트 팀 기준 |
|-------|-----|-------------|
| 배포 빈도 | 프로덕션 배포 빈도 | 하루 여러 번 |
| 리드 타임 | 커밋→프로덕션 시간 | 1시간 미만 |
| 변경 실패율 | 배포 후 장애 비율 | 0~15% |
| 복구 시간 | 장애→복구 시간 | 1시간 미만 |

### 설계 원칙 관계
| 원칙 | 목표 | 관련 실천법 |
|-----|-----|----------|
| 모듈성 | 독립적 변경 단위 | 마이크로서비스, 패키지 구조 |
| 응집도 | 관련 기능 집중 | SRP, 도메인 패키징 |
| 관심사 분리 | 독립적 이해/변경 | 레이어, 포트와 어댑터 |
| 정보 은닉 | 변경 영향 최소화 | 캡슐화, 인터페이스 |
| 결합도 관리 | 의존성 최소화 | DIP, 이벤트 기반 |

---

## 파일 구조
```
chXX-{chapter-name}/
  ├── map.md           # 챕터별 개념 맵 (TL;DR, Key Ideas, Trade-offs, Open Questions)
  └── qa.md            # 챕터별 Q&A
```
