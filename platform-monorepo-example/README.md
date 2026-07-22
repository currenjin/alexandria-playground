# platform-monorepo-example

**2026-07-22 결정된 레포/플랫폼 구조를 보여주는 예시.** 기존 [`event-backbone-example`](../event-backbone-example)의 이벤트 백본 코드를 그대로(자바 패키지 불변) 새 구조로 재배치했다.

> ⚠️ **구조는 아직 확정 아님 — "보여주기용".** 결정 근거: `design/framework/`의 `공통-자체구현-요건.md`·`공통-운영모델.md`·`공통-고민.md #32`·`개발-컨벤션-표준.md §1·§2`.

## 반영된 결정

- **모노레포 + 비즈니스 모듈별 풀스택 분할** — `oms/tms/bms/ems`가 각각 `backend`+`frontend`(풀스택 개발자 소유). `cim`은 backend(이 예제엔 미포함).
- **공통 = `platform/`** (같은 레포의 공통 모듈). 지금은 모듈 backend가 **소스로 직접 의존**(`project(':platform:core')`). 버전·레지스트리·인증 불필요.
- **단일 브랜치(`main`) + Git 태그 배포** — 바뀐 모듈만, 스테이징·상용은 같은 태그 아티팩트로 승격. 핫픽스는 단수명 hotfix 브랜치→태그→main 머지.

> platform을 **버전 아티팩트(GitHub Packages + BOM)**로 게시·소비하는 "아티팩트 모드"는 모듈이 서로 다른 platform 버전을 쓰거나 외부 소비자가 생길 때 도입한다(현재는 불필요 — `공통-고민.md #32`). 지금 예제는 소스 직접 의존만 담는다.

## 구조

```
platform-monorepo-example/
├─ settings.gradle · build.gradle          # 단일 빌드 + 공통 규약
├─ pnpm-workspace.yaml                      # */frontend 워크스페이스
├─ docker-compose.yml · docker/ · Dockerfile
├─ .github/workflows/                       # ci · deploy-module(태그)
├─ platform/                     # 공통 (비즈 모듈처럼 backend/frontend로 가름)
│  ├─ backend/                   # 공통 BE (Gradle)
│  │  └─ core · common · contract · orchestrator · gateway(Spring Cloud Gateway)
│  ├─ frontend/                  # 공통 FE (pnpm)
│  │  └─ ui(@wemeet/ui) · lib(@wemeet/lib)
│  └─ monitor/                   # 데모 대시보드(python, ops 도구 — 분할 밖)
├─ oms/ { backend · frontend }   # 비즈니스 모듈 = 풀스택
├─ tms/ { backend · frontend }
├─ bms/ { backend · frontend }
└─ ems/ { backend · frontend }
```

## 빌드·테스트

```bash
./gradlew build          # 전 모듈 컴파일 + bootJar + 라이브러리 jar
./gradlew test           # 단위 + Testcontainers 통합(oms·ems: Outbox→Kafka→Inbox 왕복)
```
검증(2026-07-22): `build` · `test` 모두 SUCCESSFUL(core 단위·orchestrator 사가·oms 통합 IT 통과).

## 모듈 backend가 platform 쓰는 법

```groovy
dependencies {
    implementation project(':platform:core')
    implementation project(':platform:contract')
}
```
```java
@SpringBootApplication
@EnableEventBackbone          // 스위치 한 줄 → platform 빈이 auto-config로 올라옴
class OmsApplication {}

@Service
class OrderService {
    private final EventPublisher events;   // 생성자 주입(platform 빈)
    @Transactional void confirm(Order o){ o.confirm(); events.publish(new OrderConfirmed(o.getId())); }
}
```

## 프론트엔드 (pnpm 워크스페이스)

- 각 모듈 frontend = 독립 Vite(CSR) 앱. `pnpm --filter @wemeet/oms-frontend build`.
- 공통 컴포넌트·유틸 = `platform/frontend/ui`(`@wemeet/ui`)·`platform/frontend/lib`(`@wemeet/lib`). 모듈은 `workspace:*`로 소비 = BE의 `project(':platform:backend:core')`와 **대칭**(레지스트리 안 거치고 로컬 링크, 항상 최신).

```jsonc
// oms/frontend/package.json
"dependencies": { "@wemeet/ui": "workspace:*", "@wemeet/lib": "workspace:*" }
```
```tsx
import { Button } from "@wemeet/ui";       // 공통 컴포넌트
import { formatMoney } from "@wemeet/lib";  // 공통 유틸
```

- **BE(Gradle) ↔ FE(pnpm)는 도구가 분리**: `settings.gradle`은 `:platform:backend:*` Gradle 모듈만, `pnpm-workspace.yaml`은 `platform/frontend/*`·`*/frontend`만. 한 `platform/` 아래 `backend/`·`frontend/`로 갈라 공존.

## IDE (IntelliJ IDEA Ultimate)

한 창에서 **BE(Gradle) + FE(JS/TS/pnpm) 둘 다** 인식(WebStorm 내장). 레포 루트를 열면:
1. Gradle 임포트 → `:platform:backend:*`·`:*:backend` BE 모듈 자동 인식.
2. `Settings → Languages & Frameworks → Node.js`: Node interpreter + **package manager = pnpm**.
3. `tsconfig.base.json`(루트) + 각 `*/frontend`·`platform/frontend/*`의 `tsconfig.json` → FE를 TS 프로젝트로 인식, `workspace:*` 참조 해석.
4. (선택) 각 `build/`·`node_modules`·`dist` Mark as Excluded로 인덱싱 부담↓.

## 원본 대비 매핑

| 기존 event-backbone-example | 이 예제 |
| --- | --- |
| `platforms/{core,common,contract,orchestrator}` | `platform/backend/{core,common,contract,orchestrator}` + `platform/frontend/{ui,lib}` |
| `services/{oms,tms,bms,ems}` | `{oms,tms,bms,ems}/backend` (+ `/frontend` 신규) |
| `com.wemeet` group 단일 | platform=`com.wemeet.platform` / 비즈니스=`com.wemeet` |
