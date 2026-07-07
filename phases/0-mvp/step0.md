# Step 0: project-setup

## 읽어야 할 파일

먼저 아래 파일들을 읽고 프로젝트의 아키텍처와 설계 의도를 파악하라:

- `/docs/ARCHITECTURE.md`
- `/docs/ADR.md`
- `/CLAUDE.md`

## 작업

Maven WAR 프로젝트의 뼈대를 만든다. 기능 코드(컨트롤러/뷰)는 만들지 않는다.

1. `pom.xml` 생성:
   - `groupId=com.haksuperman`, `artifactId=hello-cicd-web`, `version=1.0.0`, `packaging=war`
   - properties: `maven.compiler.release=11`, 소스 인코딩 UTF-8
   - 의존성: `spring-webmvc 5.3.39`, `javax.servlet:javax.servlet-api 4.0.1 (provided)`, `jstl 1.2`, `logback-classic 1.2.13`, 테스트용 `junit-jupiter 5.10.2` + `spring-test 5.3.39` + `hamcrest`
   - 플러그인: `maven-war-plugin`(`failOnMissingWebXml=false`), `maven-surefire-plugin 3.2.5`(JUnit 5 실행)
   - `<finalName>hello-cicd-web</finalName>` — 산출물은 `target/hello-cicd-web.war`
2. `src/main/java/com/haksuperman/web/WebInitializer.java` — `WebApplicationInitializer` 구현. `AnnotationConfigWebApplicationContext`에 `SpringConfig`를 등록하고 `DispatcherServlet`을 `/`에 매핑, `loadOnStartup=1`.
3. `src/main/java/com/haksuperman/web/config/SpringConfig.java` — `@Configuration` + `@EnableWebMvc` + `@ComponentScan("com.haksuperman.web")`, `InternalResourceViewResolver`(prefix `/WEB-INF/views/`, suffix `.jsp`).
4. `src/main/resources/logback.xml` — 콘솔 어펜더, 루트 레벨 INFO.

## Acceptance Criteria

```bash
docker run --rm -v "$PWD":/app -v hello-cicd-web-m2:/root/.m2 -w /app maven:3.9-eclipse-temurin-17 mvn -q package
ls target/hello-cicd-web.war
```

## 검증 절차

1. 위 AC 커맨드를 실행한다. (첫 실행은 의존성 다운로드로 수 분 걸릴 수 있다)
2. 아키텍처 체크리스트를 확인한다:
   - ARCHITECTURE.md 디렉토리 구조를 따르는가?
   - ADR 기술 스택을 벗어나지 않았는가?
   - CLAUDE.md CRITICAL 규칙을 위반하지 않았는가?
3. 결과에 따라 `phases/0-mvp/index.json`의 해당 step을 업데이트한다:
   - 성공 → `"status": "completed"`, `"summary": "산출물 한 줄 요약"`
   - 수정 3회 시도 후에도 실패 → `"status": "error"`, `"error_message": "구체적 에러 내용"`
   - 사용자 개입 필요 (API 키, 외부 인증, 수동 설정 등) → `"status": "blocked"`, `"blocked_reason": "구체적 사유"` 후 즉시 중단

## 금지사항

- Spring Boot 의존성(`spring-boot-*`)을 추가하지 마라. 이유: ADR-001 — 순수 Spring MVC + 외부 Tomcat WAR 구조를 유지해야 강의 CI/CD 단계가 그대로 적용된다.
- 로컬 `mvn`/`java` 명령을 직접 실행하지 마라. 이유: 로컬에 JDK/Maven이 없다. 빌드는 반드시 위 Docker 커맨드로.
- `web.xml`을 만들지 마라. 이유: WebInitializer(Java 설정)로 대체하고 war plugin의 `failOnMissingWebXml=false`로 처리한다.
- 컨트롤러, JSP, version.properties를 만들지 마라. 이유: step 1~2의 범위다.
- 기존 테스트를 깨뜨리지 마라
