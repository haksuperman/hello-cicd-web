# Architecture Decision Records

## 철학
강의 레포(joneconsulting/cicd-web-project)와 빌드·배포 특성을 동일하게 유지한다. 앱은 최소한으로 — 이 프로젝트의 산출물은 기능이 아니라 CI/CD 파이프라인 실습 재료다.

---

### ADR-001: Spring MVC 5.3 + WAR + 외부 Tomcat 9 (Spring Boot 미사용)
**결정**: Spring Boot 없이 순수 Spring MVC를 Java 설정(WebApplicationInitializer)으로 구성하고, WAR로 패키징해 외부 Tomcat 9에 배포한다.
**이유**: 강의의 모든 실습 단계(Jenkins Maven 빌드, WAR → Tomcat 배포, tomcat:9.0 기반 Docker 이미지)가 내 레포에 그대로 적용된다.
**트레이드오프**: 내장 서버·자동 설정 등 Spring Boot의 개발 편의성을 포기한다.

### ADR-002: Docker 전용 빌드 환경
**결정**: 로컬에 JDK/Maven을 설치하지 않고, `maven:3.9-eclipse-temurin-17` 이미지로 빌드·테스트한다. Maven 로컬 저장소는 `hello-cicd-web-m2` 명명 볼륨으로 캐시한다.
**이유**: 로컬 머신에 Docker만 설치되어 있고, Jenkins 실습 환경도 컨테이너 기반이라 일관성이 있다.
**트레이드오프**: 첫 빌드가 느리다(의존성 다운로드). IDE의 직접 빌드·디버깅 지원을 포기한다.

### ADR-003: Java 21 바이트코드 타깃 (maven.compiler.release=21)
**결정**: `maven:3.9-eclipse-temurin-21`로 빌드하고 바이트코드는 Java 21 타깃으로 컴파일한다. (최초 Java 11에서 2026-07-07 상향)
**이유**: 최신 LTS 언어 기능(record 등)을 쓸 수 있다. 런타임인 `tomcat:9.0` 이미지는 JDK 25(Temurin)를 포함하므로 Java 21 클래스가 그대로 동작하며, Spring 5.3.x는 JDK 21까지 공식 지원한다.
**트레이드오프**: 빌드 환경(Jenkins 에이전트 등)에 JDK 21 이상이 필요하다. 강의 실습 환경의 JDK가 낮으면 release를 낮추거나 Jenkins에 JDK 21을 설치해야 한다.

### ADR-004: 버전·빌드 시각은 Maven resource filtering으로 주입
**결정**: `version.properties`에 `${project.version}`과 `${maven.build.timestamp}`를 필터링으로 주입하고, 컨트롤러가 읽어 홈 페이지에 표시한다.
**이유**: 배포된 바이너리가 어떤 빌드인지 페이지에서 즉시 확인할 수 있어야 CI/CD 실습에서 배포 성공을 검증할 수 있다.
**트레이드오프**: 없음에 가깝다 (표준 Maven 기능).
