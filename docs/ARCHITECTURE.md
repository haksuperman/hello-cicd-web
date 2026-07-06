# 아키텍처

## 디렉토리 구조
```
src/
├── main/
│   ├── java/com/haksuperman/web/
│   │   ├── WebInitializer.java        # Servlet 3.x 부트스트랩 (web.xml 대체)
│   │   ├── config/SpringConfig.java   # @EnableWebMvc, ViewResolver 설정
│   │   └── controller/                # WelcomeController, HealthController
│   ├── resources/
│   │   ├── logback.xml
│   │   └── version.properties         # Maven 필터링으로 버전·빌드시각 주입
│   └── webapp/WEB-INF/views/          # index.jsp
└── test/java/com/haksuperman/web/     # MockMvc 기반 컨트롤러 테스트
Dockerfile                              # tomcat:9.0 + target/*.war 복사
pom.xml                                 # packaging=war, finalName=hello-cicd-web
```

## 패턴
- Spring MVC 컨트롤러 → JSP 뷰 (InternalResourceViewResolver, `/WEB-INF/views/*.jsp`)
- JSON 응답(/health)은 뷰 없이 `@ResponseBody`로 직접 반환
- 계층 분리 없음 — 서비스/리포지토리 레이어를 만들지 않는다 (기능이 그럴 규모가 아님)

## 데이터 흐름
```
브라우저 요청 → Tomcat → DispatcherServlet → Controller
  → (홈) Model에 msg/today/version 담아 index.jsp 렌더링
  → (/health) JSON 문자열 직접 응답
```

## 상태 관리
- 무상태. DB·세션·캐시 없음. 버전 정보는 클래스패스의 version.properties에서 기동 시 1회 로드.
