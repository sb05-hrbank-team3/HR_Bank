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


# {팀 이름}

[팀 협업 문서 링크](#)

## 팀원 구성
| 이름  | 역할          | 설명                                             | GitHub 주소                              |
| --- | ----------- | ---------------------------------------------- | -------------------------------------- |
| 남현수 | (팀장) 백엔드 개발자     | 직원 관리 시스템 엔티티 설계 및 구현, CI/CD 파이프라인 구축, 환경변수 관리 | [GitHub](https://github.com/Namsoo315) |
| 류승민 | 백엔드 개발자     | 부서 관리 및 데이터 백업 시스템 개발, Batch 처리 및 스케줄링 담당      | [GitHub](https://github.com/bustam00)  |
| 박종현 | 백엔드 개발자     | 수정 로그 시스템 개발, ERD 설계 및 코드 리팩터링                 | [GitHub](https://github.com/yeahlimm)  |
| 이예림 | 백엔드/프론트 개발자 | 프론트엔드와 수정 상세 내역 조회 기능 개발, PPT 및 작업 흐름도 제작      | [GitHub](https://github.com/Namsoo315) |
| 민재영 | 백엔드/프론트 개발자 | 프론트엔드 및 데이터 백업 기능 개발, 파일 스토리지 설정 및 관리          | [GitHub](https://github.com/jymin0)    |



## 프로젝트 소개
프로그래밍 교육 사이트의 Spring 백엔드 시스템 구축  

- **프로젝트 기간:** 2024.08.13 ~ 2024.09.03  

### 기술 스택
- **Backend:** Spring Boot, Spring Security, Spring Data JPA  
- **Database:** MySQL  
- **공통 Tool:** Git & GitHub, Discord  

## 팀원별 구현 기능 상세

### 웨인
(자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)

- **소셜 로그인 API**  
  Google OAuth 2.0을 활용한 소셜 로그인 기능 구현  
  로그인 후 추가 정보 입력을 위한 RESTful API 엔드포인트 개발  

- **회원 추가 정보 입력 API**  
  회원 유형(관리자, 학생)에 따른 조건부 입력 처리 API 구현  

### 제이든
(자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)

- **회원별 권한 관리**  
  Spring Security를 활용하여 사용자 역할에 따른 권한 설정  
  관리자 페이지와 일반 사용자 페이지를 위한 조건부 라우팅 처리  

- **반응형 레이아웃 API**  
  클라이언트에서 요청된 반응형 레이아웃을 위한 RESTful API 엔드포인트 구현  

### 마크
(자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)

- **수강생 정보 관리 API**  
  GET 요청을 사용하여 학생의 수강 정보를 조회하는 API 엔드포인트 개발  
  학생 정보의 CRUD 처리 (Spring Data JPA 사용)  

- **공용 Button API**  
  공통으로 사용할 버튼 기능을 처리하는 API 엔드포인트 구현  

### 데이지
(자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)

- **관리자 API**  
  @PathVariable을 사용한 동적 라우팅 기능 구현  
  PATCH, DELETE 요청을 사용하여 학생 정보를 수정하고 탈퇴하는 API 엔드포인트 개발  

- **CRUD 기능**  
  학생 정보의 CRUD 기능을 제공하는 API 구현 (Spring Data JPA)  

- **회원관리 슬라이더**  
  학생별 정보 목록을 Carousel 형식으로 조회하는 API 구현  

### 제이
(자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)

- **학생 시간 정보 관리 API**  
  학생별 시간 정보를 GET 요청을 사용하여 조회하는 API 구현  
  실시간 접속 현황을 관리하는 API 엔드포인트  

- **수정 및 탈퇴 API**  
  PATCH, DELETE 요청을 사용하여 수강생의 개인정보 수정 및 탈퇴 처리  

- **공용 Modal API**  
  공통 Modal 컴포넌트를 처리하는 API 구현  

## 파일 구조
