# Step 2: health-endpoint

## 읽어야 할 파일

먼저 아래 파일들을 읽고 프로젝트의 아키텍처와 설계 의도를 파악하라:

- `/docs/ARCHITECTURE.md`
- `/docs/ADR.md`
- `src/main/java/com/haksuperman/web/controller/WelcomeController.java`, `src/test/java/com/haksuperman/web/controller/WelcomeControllerTest.java` (step 1 산출물)

이전 step에서 만들어진 코드를 꼼꼼히 읽고, 설계 의도를 이해한 뒤 작업하라.

## 작업

배포 확인 및 Kubernetes probe 실습용 `/health` 엔드포인트를 만든다. CLAUDE.md의 TDD 규칙에 따라 **테스트를 먼저 작성**하라.

1. 테스트 `src/test/java/com/haksuperman/web/controller/HealthControllerTest.java`:
   - MockMvc `standaloneSetup` 사용. GET `/health` → status 200, Content-Type이 `application/json` 호환, 본문에 `"UP"` 포함.
2. `src/main/java/com/haksuperman/web/controller/HealthController.java`:
   - `@Controller`, `@GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)` + `@ResponseBody` → `{"status":"UP"}` 문자열 반환.
   - CRITICAL: Jackson 등 JSON 라이브러리를 추가하지 말고 고정 문자열로 반환하라. 이유: 고정 JSON 하나에 직렬화 라이브러리는 과하다 (ADR 철학: 최소 구현).

## Acceptance Criteria

```bash
docker run --rm -v "$PWD":/app -v hello-cicd-web-m2:/root/.m2 -w /app maven:3.9-eclipse-temurin-17 mvn -q test
```

## 검증 절차

1. 위 AC 커맨드를 실행한다.
2. 아키텍처 체크리스트를 확인한다:
   - ARCHITECTURE.md 디렉토리 구조를 따르는가?
   - ADR 기술 스택을 벗어나지 않았는가?
   - CLAUDE.md CRITICAL 규칙을 위반하지 않았는가?
3. 결과에 따라 `phases/0-mvp/index.json`의 해당 step을 업데이트한다:
   - 성공 → `"status": "completed"`, `"summary": "산출물 한 줄 요약"`
   - 수정 3회 시도 후에도 실패 → `"status": "error"`, `"error_message": "구체적 에러 내용"`
   - 사용자 개입 필요 → `"status": "blocked"`, `"blocked_reason": "구체적 사유"` 후 즉시 중단

## 금지사항

- `WelcomeController`와 `index.jsp`를 수정하지 마라. 이유: 이 step은 /health만 다룬다.
- 새 의존성(Jackson 포함)을 추가하지 마라. 이유: 고정 문자열 반환으로 충분하다.
- 로컬 `mvn`/`java` 명령을 직접 실행하지 마라. 이유: 로컬에 JDK/Maven이 없다.
- 기존 테스트를 깨뜨리지 마라
