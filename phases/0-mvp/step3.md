# Step 3: docker-image

## 읽어야 할 파일

먼저 아래 파일들을 읽고 프로젝트의 아키텍처와 설계 의도를 파악하라:

- `/CLAUDE.md` (명령어 섹션)
- `/docs/PRD.md`
- `pom.xml` (finalName 확인)

이전 step에서 만들어진 코드를 꼼꼼히 읽고, 설계 의도를 이해한 뒤 작업하라.

## 작업

레포 루트에 `Dockerfile`을 만든다. 강의 레포(joneconsulting/cicd-web-project)와 동일하게 **사전 빌드된 WAR을 복사하는 방식**이다:

```dockerfile
FROM tomcat:9.0
COPY target/hello-cicd-web.war /usr/local/tomcat/webapps/
```

컨텍스트 경로는 `/hello-cicd-web`이 된다 (WAR 파일명 기준).

## Acceptance Criteria

```bash
docker run --rm -v "$PWD":/app -v hello-cicd-web-m2:/root/.m2 -w /app maven:3.9-eclipse-temurin-17 mvn -q package
docker build -t hello-cicd-web .
docker rm -f hello-cicd-web-test 2>/dev/null || true
docker run -d --name hello-cicd-web-test -p 8080:8080 hello-cicd-web
for i in $(seq 1 30); do curl -fs http://localhost:8080/hello-cicd-web/health >/dev/null && break; sleep 1; done
curl -fs http://localhost:8080/hello-cicd-web/health | grep -q UP
curl -fs http://localhost:8080/hello-cicd-web/ | grep -q '1.0.0'
docker rm -f hello-cicd-web-test
```

## 검증 절차

1. 위 AC 커맨드를 순서대로 실행한다. (Tomcat 기동에 수 초 걸리므로 curl 재시도 루프를 사용한다)
2. 아키텍처 체크리스트를 확인한다:
   - ADR 기술 스택을 벗어나지 않았는가? (tomcat:9.0 베이스)
   - CLAUDE.md CRITICAL 규칙을 위반하지 않았는가?
3. 결과에 따라 `phases/0-mvp/index.json`의 해당 step을 업데이트한다:
   - 성공 → `"status": "completed"`, `"summary": "산출물 한 줄 요약"`
   - 수정 3회 시도 후에도 실패 → `"status": "error"`, `"error_message": "구체적 에러 내용"`
   - 사용자 개입 필요 (예: 8080 포트 충돌) → `"status": "blocked"`, `"blocked_reason": "구체적 사유"` 후 즉시 중단

## 금지사항

- 멀티스테이지 Docker 빌드로 바꾸지 마라. 이유: 강의 파이프라인은 Jenkins가 WAR을 빌드한 뒤 이미지를 만드는 구조라서, 이미지 빌드 시점에 WAR이 이미 존재해야 한다.
- 애플리케이션 코드(src/)를 수정하지 마라. 이유: 이 step은 패키징만 다룬다.
- 검증에 사용한 컨테이너(`hello-cicd-web-test`)를 반드시 정리하라. 이유: 다음 실행 시 이름/포트 충돌이 난다.
- 기존 테스트를 깨뜨리지 마라
