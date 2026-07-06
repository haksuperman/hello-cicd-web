# PRD: hello-cicd-web

## 목표
Jenkins·Docker·Kubernetes CI/CD 파이프라인 실습의 배포 대상이 되는 최소 웹 애플리케이션. 앱 자체가 아니라 "빌드 → 테스트 → 패키징 → 배포" 파이프라인이 주인공이다.

## 사용자
- 본인 (CI/CD 강의 수강자). 배포가 성공했는지 브라우저와 curl로 즉시 확인할 수 있어야 한다.

## 핵심 기능
1. 홈 페이지(`/`): 인사 메시지 + 오늘 날짜 + 앱 버전·빌드 시각 표시 — 새 버전이 배포됐는지 눈으로 확인하는 용도
2. `/health`: `{"status":"UP"}` 형태의 JSON 응답 — 배포 후 확인 및 Kubernetes liveness/readiness probe 실습용
3. Maven WAR 패키징 + Dockerfile(`tomcat:9.0` 기반) — Jenkins 빌드·Tomcat 배포·Docker 이미지 실습의 재료

## MVP 제외 사항
- 데이터베이스, 영속성 계층
- 로그인/인증
- 프론트엔드 프레임워크(React 등) 및 프론트 빌드 도구 — JSP로 충분
- 기능성 REST API — /health 외 API 없음

## 디자인
- 미니멀. 시스템 폰트, 라이트 배경, 장식 없음
- 빌드 정보(버전·시각)는 표 형태로 명확하게
