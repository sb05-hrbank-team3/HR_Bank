# HR_Bank
Batch로 데이터를 관리하는 Open EMS

> 🏢 기업의 핵심 자산, 인적 자원을 체계적으로 관리하세요!
HR Bank는 인사 데이터를 안전하고 효율적으로 관리할 수 있도록 설계된 Open EMS(Enterprise Management System)입니다. 대량의 데이터를 안정적으로 처리할 수 있는 Batch 시스템을 기반으로 부서 및 직원 정보를 체계적으로 운영할 수 있으며, 백업 자동화, 이력 관리, 대시보드 제공을 통해 기업 인사 관리를 더욱 효과적으로 지원합니다. 📊💼

---
## 📦 Dependencies

### ✅ Spring Boot Starters

* `spring-boot-starter-web` : REST API 및 웹 애플리케이션 개발
* `spring-boot-starter-data-jpa` : JPA 기반 ORM (Hibernate 포함)
* `spring-boot-starter-batch` : Spring Batch 배치 처리
* `spring-boot-starter-test` : 통합 테스트 및 단위 테스트 지원
* `spring-batch-test` : 배치 테스트 유틸리티

### ✅ 데이터베이스

* `org.postgresql:postgresql` : PostgreSQL 데이터베이스 드라이버
* `com.h2database:h2` : 인메모리 H2 데이터베이스 (테스트용)

### ✅ 개발 편의성

* `spring-boot-devtools` : 핫 리로드 및 개발 환경 편의 기능
* `lombok` : 보일러플레이트 코드 제거 (Getter/Setter, Builder 등)

### ✅ API 문서화

* `springdoc-openapi-starter-webmvc-ui:2.8.9` : OpenAPI 3.0(Swagger UI) 지원

### ✅ 매핑/변환

* `mapstruct` : DTO ↔ Entity 매핑 자동화 라이브러리

### ✅ 테스트 도구

* `junit-platform-launcher` : JUnit 실행 런처 (테스트 런타임 전용)

