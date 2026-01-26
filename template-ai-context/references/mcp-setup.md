# MCP (Model Context Protocol) 설정 가이드

Claude Code에서 JIRA 등 외부 도구와 연동하기 위한 MCP 설정 방법입니다.

## MCP란?

MCP(Model Context Protocol)는 AI가 외부 도구(JIRA, Confluence 등)와 통신할 수 있게 해주는 프로토콜입니다.

## JIRA 연동 설정

### 1. JIRA API 토큰 발급

1. [Atlassian API 토큰 관리](https://id.atlassian.com/manage-profile/security/api-tokens) 접속
2. "Create API token" 클릭
3. 토큰 이름 입력 (예: "Claude Code")
4. 생성된 토큰 복사 (다시 볼 수 없으니 안전한 곳에 저장)

### 2. MCP 서버 추가

```bash
claude mcp add atlassian \
  -e ATLASSIAN_BASE_URL=https://wemeet2025.atlassian.net \
  -e ATLASSIAN_EMAIL=your-email@wemeetmobility.com \
  -e ATLASSIAN_API_TOKEN=your-api-token \
  -- /path/to/mcp-atlassian
```

> **참고**: `npx -y mcp-atlassian`은 의존성 문제가 있어 전역 설치 후 절대 경로 사용을 권장합니다.
> ```bash
> npm install -g mcp-atlassian
> cd $(npm root -g)/mcp-atlassian && npm install jsdom
> which mcp-atlassian  # 경로 확인
> ```

### 3. 설정 확인

```bash
claude mcp list
```

### 4. Claude Code 재시작

설정 후 Claude Code를 재시작하면 MCP 서버가 자동으로 연결됩니다.

## MCP 관리 명령어

| 명령어 | 설명 |
|--------|------|
| `claude mcp list` | 설정된 MCP 서버 목록 |
| `claude mcp get atlassian` | 특정 서버 상세 정보 |
| `claude mcp remove atlassian` | 서버 제거 |

## 사용 가능한 기능

MCP 연동 후 사용 가능한 skill:

| Skill | 설명 |
|-------|------|
| `/plan-from-jira RP-1234` | JIRA 티켓을 읽어서 TDD plan 생성 |

## 문제 해결

### MCP 서버 연결 실패

1. Node.js가 설치되어 있는지 확인
2. `npx` 명령어가 동작하는지 확인
3. API 토큰이 유효한지 확인
4. JIRA URL이 올바른지 확인

### 권한 오류

- JIRA 프로젝트에 대한 접근 권한이 있는지 확인
- API 토큰을 발급한 계정이 해당 프로젝트에 접근 가능한지 확인

## 참고 자료

- [Atlassian MCP Server (공식)](https://github.com/atlassian/atlassian-mcp-server)
- [mcp-atlassian (커뮤니티)](https://github.com/sooperset/mcp-atlassian)
