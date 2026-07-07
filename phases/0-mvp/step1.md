# Step 1: welcome-page

## 읽어야 할 파일

먼저 아래 파일들을 읽고 프로젝트의 아키텍처와 설계 의도를 파악하라:

- `/docs/ARCHITECTURE.md`
- `/docs/ADR.md`
- `/docs/PRD.md`
- `pom.xml`, `src/main/java/com/haksuperman/web/WebInitializer.java`, `src/main/java/com/haksuperman/web/config/SpringConfig.java` (step 0 산출물)

이전 step에서 만들어진 코드를 꼼꼼히 읽고, 설계 의도를 이해한 뒤 작업하라.

## 작업

홈 페이지를 만든다. CLAUDE.md의 TDD 규칙에 따라 **테스트를 먼저 작성**하라.

1. `pom.xml`에 리소스 필터링 추가: `src/main/resources`에 `filtering=true`.
   - CRITICAL: `${maven.build.timestamp}`는 리소스 필터링에서 직접 치환되지 않는 Maven의 알려진 제약이 있다. 반드시 pom properties에 `<build.timestamp>${maven.build.timestamp}</build.timestamp>`를 정의하고, properties 파일에서는 `${build.timestamp}`를 참조하라. `<maven.build.timestamp.format>yyyy-MM-dd HH:mm:ss</maven.build.timestamp.format>`도 함께 정의하라.
2. `src/main/resources/version.properties`:
   ```properties
   app.version=${project.version}
   build.timestamp=${build.timestamp}
   ```
3. 테스트 `src/test/java/com/haksuperman/web/controller/WelcomeControllerTest.java`:
   - MockMvc `standaloneSetup` 사용. GET `/` → status 200, view name `"index"`, model에 `msg`·`today`·`version`·`buildTimestamp` 속성 존재 확인.
4. `src/main/java/com/haksuperman/web/controller/WelcomeController.java`:
   - `@Controller`, `@GetMapping("/")` → model에 `msg`(인사 문구), `today`(현재 날짜), `version`, `buildTimestamp`(클래스패스의 version.properties에서 로드)를 담고 `"index"` 반환.
   - version.properties 로드는 생성자에서 1회. 파일이 없으면 `"unknown"`으로 폴백.
5. `src/main/webapp/WEB-INF/views/index.jsp`:
   - msg, today 표시 + 버전·빌드시각을 표(table)로 표시. PRD 디자인: 미니멀, 시스템 폰트, 라이트 배경, 장식 없음.

## Acceptance Criteria

```bash
docker run --rm -v "$PWD":/app -v hello-cicd-web-m2:/root/.m2 -w /app maven:3.9-eclipse-temurin-17 mvn -q package
grep -q 'app.version=1.0.0' target/classes/version.properties
! grep -q '\${' target/classes/version.properties
```

## 검증 절차

1. 위 AC 커맨드를 실행한다. (`mvn package`가 테스트도 함께 실행한다. grep 2개는 필터링이 실제로 동작했는지 확인한다)
2. 아키텍처 체크리스트를 확인한다:
   - ARCHITECTURE.md 디렉토리 구조를 따르는가?
   - ADR 기술 스택을 벗어나지 않았는가?
   - CLAUDE.md CRITICAL 규칙을 위반하지 않았는가?
3. 결과에 따라 `phases/0-mvp/index.json`의 해당 step을 업데이트한다:
   - 성공 → `"status": "completed"`, `"summary": "산출물 한 줄 요약"`
   - 수정 3회 시도 후에도 실패 → `"status": "error"`, `"error_message": "구체적 에러 내용"`
   - 사용자 개입 필요 → `"status": "blocked"`, `"blocked_reason": "구체적 사유"` 후 즉시 중단

## 금지사항

- `/health` 엔드포인트를 만들지 마라. 이유: step 2의 범위다.
- JSP에 스크립틀릿(`<% %>`)을 쓰지 마라. 이유: EL과 JSTL로 충분하다.
- 새 의존성을 추가하지 마라. 이유: 현재 의존성으로 전부 구현 가능하다.
- 로컬 `mvn`/`java` 명령을 직접 실행하지 마라. 이유: 로컬에 JDK/Maven이 없다.
- 기존 테스트를 깨뜨리지 마라
