# hello-cicd-web

CI/CD 파이프라인(Jenkins · Docker · Kubernetes) 학습용 데모 웹 애플리케이션입니다.

## 기술 스택

- Spring MVC 5.3 — Java 설정 기반 (Spring Boot 아님)
- Java 21 / Maven / WAR 패키징
- JSP + JSTL 뷰, Logback 로깅
- 런타임: Tomcat 9 (`tomcat:9.0` Docker 이미지)

## 엔드포인트

| 경로 | 설명 |
|------|------|
| `/` | 인사 메시지 + 오늘 날짜 + 앱 버전·빌드 시각 — 배포 성공을 눈으로 확인하는 용도 |
| `/health` | `{"status":"UP"}` JSON — 배포 확인 및 Kubernetes liveness/readiness probe용 |

WAR 파일명 기준으로 컨텍스트 경로는 `/hello-cicd-web`입니다.

## 빌드 & 실행

로컬에 JDK/Maven 설치 없이 Docker만으로 빌드·실행합니다.

```bash
# WAR 빌드 (산출물: target/hello-cicd-web.war)
docker run --rm -v "$PWD":/app -v hello-cicd-web-m2:/root/.m2 -w /app maven:3.9-eclipse-temurin-21 mvn package

# 테스트
docker run --rm -v "$PWD":/app -v hello-cicd-web-m2:/root/.m2 -w /app maven:3.9-eclipse-temurin-21 mvn test

# 이미지 빌드 & 실행 → http://localhost:8080/hello-cicd-web/
docker build -t hello-cicd-web .
docker run --rm -p 8080:8080 hello-cicd-web
```

Jenkins 등 CI 환경에서 빌드할 경우 JDK 21 이상이 필요합니다.

## 프로젝트 구조

```
src/main/java/com/haksuperman/web/
├── WebInitializer.java        # DispatcherServlet 부트스트랩 (web.xml 미사용)
├── config/SpringConfig.java   # ViewResolver 설정
└── controller/                # WelcomeController(/), HealthController(/health)
src/main/webapp/WEB-INF/views/ # index.jsp
src/test/                      # MockMvc 컨트롤러 테스트
Dockerfile                     # tomcat:9.0 베이스에 빌드된 WAR 복사
docs/                          # PRD · ADR · ARCHITECTURE 설계 문서
phases/                        # 하네스 step 설계 및 실행 기록
```

## 개발 방식

[jha0313/harness_framework](https://github.com/jha0313/harness_framework) 하네스로 구현했습니다.
`docs/`의 설계 문서를 가드레일로 삼아, `phases/`에 정의된 step을 `scripts/execute.py`가 Claude Code 헤드리스 세션으로 순차 실행하는 방식입니다. 자세한 설계 결정은 [docs/ADR.md](docs/ADR.md)를 참고하세요.
