# 프로젝트: hello-cicd-web

CI/CD 파이프라인(Jenkins, Docker, Kubernetes) 학습용 데모 웹.
joneconsulting/cicd-web-project와 동일한 빌드·배포 특성을 유지하는 것이 최우선 목표다.

## 기술 스택
- Spring MVC 5.3.x — Java 설정 기반 (Spring Boot 아님)
- Java 21 (maven.compiler.release=21), Maven, WAR 패키징
- JSP + JSTL 뷰, Logback 로깅, JUnit 5 + Spring Test(MockMvc)
- 런타임: Tomcat 9 (Docker 이미지 `tomcat:9.0`)

## 아키텍처 규칙
- CRITICAL: Spring Boot 의존성을 추가하지 마라. 이유: 강의 레포와 동일한 순수 Spring MVC + 외부 Tomcat WAR 배포 구조를 유지해야 CI/CD 실습 단계가 그대로 적용된다.
- CRITICAL: 빌드·테스트는 반드시 Docker maven 이미지로 실행하라. 이유: 로컬에 JDK/Maven이 설치되어 있지 않다.
- 컨트롤러는 `src/main/java/**/controller/`, JSP 뷰는 `src/main/webapp/WEB-INF/views/`에 둔다.
- 뷰가 필요 없는 엔드포인트(/health)는 `@ResponseBody`로 JSON을 반환한다.

## 개발 프로세스
- CRITICAL: 새 기능 구현 시 반드시 테스트를 먼저 작성하고, 테스트가 통과하는 구현을 작성할 것 (TDD)
- 커밋 메시지는 conventional commits 형식을 따를 것 (feat:, fix:, docs:, refactor:)

## 명령어
```bash
# WAR 빌드 (산출물: target/hello-world.war — 강의 레포와 동일한 이름/컨텍스트 경로)
docker run --rm -v "$PWD":/app -v hello-cicd-web-m2:/root/.m2 -w /app maven:3.9-eclipse-temurin-21 mvn package

# 테스트
docker run --rm -v "$PWD":/app -v hello-cicd-web-m2:/root/.m2 -w /app maven:3.9-eclipse-temurin-21 mvn test

# 로컬 실행 → http://localhost:8080/hello-world/
docker build -t hello-cicd-web . && docker run --rm -p 8080:8080 hello-cicd-web
```
