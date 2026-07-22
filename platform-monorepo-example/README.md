# platform-monorepo-example

**2026-07-22 확정 레포 구조 예시.** 기존 이벤트 백본 예제를 최상위 `platforms/` + `services/` 구조로 재배치했다.

> 결정 근거: `design/framework/`의 `공통-자체구현-요건.md`·`공통-확정.md §10`·`개발-컨벤션-표준.md`.

## 구조

```
platform-monorepo-example/
├── settings.gradle · build.gradle · tsconfig.base.json · .nvmrc
├── package.json · pnpm-workspace.yaml
├── docker-compose.yml · Dockerfile · docker/
├── .github/workflows/ { ci · deploy-module }
│
├── platforms/                       # 공통 (팀이 당겨 쓰는 것)
│   ├── backend/                     #   Gradle
│   │   ├── core · common · contract · orchestrator · gateway
│   └── frontend/                    #   pnpm
│       ├── ui/     @wemeet/ui   (Button · AppLayout(GNB) · menu)
│       ├── lib/    @wemeet/lib  (apiClient · formatMoney · getSession · tabsCookie)
│       └── shell/  @wemeet/shell (FE 단일 진입점 호스트 — AppLayout 안에 모듈 조합)
│   └── (monitor/ 데모 대시보드)
│
└── services/                        # 비즈니스 모듈 = 풀스택
    ├── oms/ { backend(레이어드) · frontend(Next.js) }
    ├── tms/ { backend · frontend }
    ├── bms/ { backend · frontend }
    └── ems/ { backend · frontend }
```

- Gradle: `:platforms:backend:{core,common,contract,orchestrator,gateway}` + `:services:{oms,tms,bms,ems}:backend`
- pnpm: `platforms/frontend/*`(ui·lib·shell) + `services/*/frontend`
- 소스 직접 의존: BE=`project(':platforms:backend:core')` · FE=`workspace:*`. 단일 브랜치 + 태그 배포.

## 빌드·테스트

```bash
./gradlew build          # BE 전 모듈 (단위 + Testcontainers 통합)
pnpm install && pnpm -r --if-present build   # FE 전 모듈 Next build
```
검증(2026-07-22): BE `build` GREEN · FE shell·oms build GREEN · orchestrator·gateway 부팅 확인.

## FE (pnpm 워크스페이스 · 마이크로프론트엔드)

- `shell` = FE 단일 진입점(호스트). 공통 `AppLayout`(GNB) 안에 모듈 web을 조합.
- 각 모듈 `frontend` = 독립 Next.js(CSR) 앱, 공통은 `@wemeet/ui`·`@wemeet/lib`를 `workspace:*`로 소비.
- 요건 스택: Next.js(CSR) · TS strict · Zustand · React Hook Form · Tailwind · Shadcn(@wemeet/ui). (AG Grid·Mapbox는 의존 선언만)

## 원본 대비

| 기존 event-backbone-example | 이 예제 |
| --- | --- |
| `platforms/{core,...}` | `platforms/backend/{core,common,contract,orchestrator,gateway}` |
| `services/{oms,...}` | `services/{oms,tms,bms,ems}/{backend,frontend}` |
| (FE 없음) | `platforms/frontend/{ui,lib,shell}` + 모듈별 frontend(Next.js) |
