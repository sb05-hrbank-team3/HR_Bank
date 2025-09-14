# HR_Bank 👨‍💼🏢
**Batch 기반 인사 관리 시스템 (Open EMS)**  
> 기업의 핵심 자산, **인적 자원**을 안전하고 효율적으로 관리하세요!  
> 대용량 데이터를 안정적으로 처리할 수 있는 **Batch 시스템** 기반으로, 부서 및 직원 정보를 체계적으로 운영할 수 있습니다.  
> 또한 **백업 자동화 · 이력 관리 · 대시보드 제공**을 통해 기업 인사 관리를 더욱 효과적으로 지원합니다. 📊💼  

- **프로젝트 기간:** 2025.09.05 ~ 2025.09.16  
- **API 명세서:** [Swagger UI 바로가기 ↗](https://hrbank-production.up.railway.app/swagger-ui/index.html)
- **배포 사이트:** [바로가기 ↗](https://hrbank-production.up.railway.app/swagger-ui/index.html)





# {팀이름}

![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white) [팀 협업 문서 링크](https://www.notion.so/2680fa6d0dae80f3b316ceff3494cfe2)

## 팀원 구성
| 이름  | 역할          | 설명                                             | GitHub 주소                              |
| --- | ----------- | ---------------------------------------------- | -------------------------------------- |
| 남현수 | (팀장) 백엔드 개발자     | 직원 관리 시스템 엔티티 설계 및 구현, CI/CD 파이프라인 구축, 환경변수 관리 | [GitHub](https://github.com/Namsoo315) |
| 류승민 | 백엔드 개발자     | 부서 관리 및 데이터 백업 시스템 개발, Batch 처리 및 스케줄링 담당      | [GitHub](https://github.com/bustam00)  |
| 박종현 | 백엔드 개발자     | 수정 로그 시스템 개발, ERD 설계 및 코드 리팩터링                 | [GitHub](https://github.com/yeahlimm)  |
| 이예림 | 백엔드/프론트 개발자 | 프론트엔드와 수정 상세 내역 조회 기능 개발, PPT 및 작업 흐름도 제작      | [GitHub](https://github.com/Namsoo315) |
| 민재영 | 백엔드/프론트 개발자 | 프론트엔드 및 데이터 백업 기능 개발, 파일 스토리지 설정 및 관리          | [GitHub](https://github.com/jymin0)    |


---

## 프로젝트 소개
### HR_Bank
Batch로 데이터를 관리하는 Open EMS

> 🏢 기업의 핵심 자산, 인적 자원을 체계적으로 관리하세요!
HR Bank는 인사 데이터를 안전하고 효율적으로 관리할 수 있도록 설계된 Open EMS(Enterprise Management System)입니다. 대량의 데이터를 안정적으로 처리할 수 있는 Batch 시스템을 기반으로 부서 및 직원 정보를 체계적으로 운영할 수 있으며, 백업 자동화, 이력 관리, 대시보드 제공을 통해 기업 인사 관리를 더욱 효과적으로 지원합니다. 📊💼

- **프로젝트 기간:** 2025.09.05 ~ 2025.09.16

---
### 기술 스택
### Frontend
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)

#### Backend  
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-000000?style=for-the-badge&logo=mapstruct&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

#### Database  
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![H2](https://img.shields.io/badge/H2-004088?style=for-the-badge&logo=h2&logoColor=white)

#### CI/CD  
![Railway](https://img.shields.io/badge/Railway-0B0D0E?style=for-the-badge&logo=railway&logoColor=white)

#### 협업 Tool  
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)

---
# 팀원별 구현 기능 상세

## 남현수
## 직원 수정 (Update Employee)

### 상황

* 운영자가 직원 정보를 변경할 수 있어야 함.
* 이름, 이메일, 부서, 직위, 상태, 프로필 이미지 등 다양한 속성이 수정될 수 있음.
* 변경 이력(ChangeLog + History)도 반드시 남겨야 추후 감사 가능.

### 문제

* 이메일은 고유해야 하므로 중복 검증 필요.
* 프로필 이미지(BinaryContent)는 파일 저장소와 DB를 함께 관리해야 함.
* 기존 직원 데이터와 수정된 데이터를 비교해 **정확한 변경 이력**을 남겨야 함.

### 행동

* Service 계층에서 다음을 수행:

  * `employeeRepository.existsByEmailAndIdNot()` 으로 중복 이메일 검사
  * 파일이 포함되면 BinaryContent DB 저장 + Storage 저장 처리
  * 기존 Employee를 `toBuilder()`로 복사 → 변경 요청값만 업데이트
  * 저장 후, ChangeLog(Updated) + History 생성 → 변경된 속성만 기록

```java
Employee oldEmployee = employee.toBuilder().build();
Employee updatedEmployee = employee.toBuilder()
    .name(request.name() != null ? request.name() : employee.getName())
    ...
    .build();
Employee savedEmployee = employeeRepository.save(updatedEmployee);

ChangeLog changeLog = ChangeLogUtils.createChangeLog(ChangeLogType.UPDATED, ipAddress, request.memo(), savedEmployee);
List<History> histories = ChangeLogUtils.createHistoriesForUpdate(changeLog, oldEmployee, savedEmployee);
```

### 결과

* 운영자가 안전하게 직원 정보를 수정할 수 있음.
* 파일과 DB가 일관성 있게 처리됨.
* 변경 내역이 History 테이블에 기록되어 사후 감사 가능.

---

## 직원 삭제 (Delete Employee)

### 상황

* 직원 계정 삭제 시 관련 이력도 남겨야 함.
* 프로필 이미지(BinaryContent)도 함께 정리해야 함.

### 문제

* 단순 삭제 시 이력 추적 불가 → 누가 언제 삭제했는지 확인 어려움.
* BinaryContent orphan 데이터(DB + 파일)가 남아 관리 이슈 발생 가능.

### 행동

* Service 계층에서 두 단계 처리:

  1. `deleteEmployee()`

     * BinaryContent 존재 시 → DB 삭제 + Storage 파일 삭제
     * Employee DB 삭제
  2. `deleteEmployeeDoSaveLog()`

     * 삭제 전 Employee 전체 정보 조회(`findByIdWithRelations`)
     * ChangeLog(Deleted) 생성 + 저장
     * Employee와 ChangeLog 관계 해제(`unlinkEmployee`)
     * History 생성 (삭제된 직원의 모든 주요 필드를 before 값으로 저장)

```java
ChangeLog changeLog = ChangeLogUtils.createChangeLog(ChangeLogType.DELETED, ipAddress, null, employee);
changeLogRepository.save(changeLog);
changeLogRepository.unlinkEmployee(employee);

List<History> histories = ChangeLogUtils.createHistoriesForDelete(changeLog, employee);
historyRepository.saveAll(histories);
```

### 결과

* 직원 삭제 시, 프로필 이미지까지 깔끔하게 정리.
* ChangeLog + History를 통해 삭제 이력 보존.
* 운영자가 삭제 후에도 과거 데이터 추적 가능.
  
---
## 류승민
### 부서 관리

### 상황
- 부서 CRUD를 추가하여야 함.

### 문제
- 부서 삭제는 소속된 직원이 없는 경우에만 가능
- 목록 조회는 선택으로 조건을 받기 때문에 JpaRepository로는 한계가 있음
  
### 행동
<details>
  <summary>파일구조 보기</summary>

```java
DepartmentService에서 소속된 직원이 있는지 확인
public void deleteById(Long id) throws IllegalStateException {  
	Department department = departmentRepository.findById(id)
	.orElseThrow(() -> new NoSuchElementException("삭제 대상 부서가 없습니다: " + id));
	List<Employee> employees= employeeRepository.findAll();  
	boolean exists = employees.stream()
	.anyMatch(e -> e.getDepartment().getId().equals(id));
	if (exists) {
	    throw new IllegalStateException("소속된 직원이 1명 이상이면 삭제가 불가능합니다: " + id ); 
	}  
	departmentRepository.deleteById(id);
}

QueryDSL을 사용해서 필요한 조건으로 쿼리문 생성
if (nameOrDescription != null && !nameOrDescription.isBlank()) {
      where.and(department.name.containsIgnoreCase(nameOrDescription)
          .or(department.description.containsIgnoreCase(nameOrDescription)));
}
if (idAfter != null) {
		where.and(department.id.gt(idAfter));
}

if (sortField.equals("name") && sortDirection.equals("asc")) {
    return queryFactory
        .select(department)
        .from(department)
        .limit(size)
        .where(where)
        .orderBy(department.name.asc())
        .fetch();
}
if (sortField.equals("name") && sortDirection.equals("desc")) {
    return queryFactory
        .select(department)
        .from(department)
        .limit(size)
        .where(where)
        .orderBy(department.name.desc())
        .fetch();
}
if (sortField.equals("establishedDate") && sortDirection.equals("desc")) {
    return queryFactory
        .select(department)
        .from(department)
        .limit(size)
        .where(where)
        .orderBy(department.establishedDate.desc())
        .fetch();
}

  return queryFactory
      .select(department)
      .from(department)
      .limit(size)
      .where(where)
      .orderBy(department.establishedDate.asc())
      .fetch();
}

```
</details>

### 결과

- 불필요한 조건 제거 → 실행 쿼리 단순화.
- 참조 무결성 유지
  
---

## 민재영
## 대용량 다운로드 스트리밍 (메모리 OOM 방지)

### 상황

* 백업 CSV 파일, 프로필 이미지 등 대용량 파일을 안정적으로 다운로드해야 함.
* 서버 메모리 사용 급증 없이 다수 동시 다운로드도 견딜 수 있어야 함.

### 문제

* byte[] 기반 응답은 파일 전체를 메모리에 적재 -> **OOM/GC** 부담 및 응답 지연
* 필요 시 브라우저에서 파일명, 용량 정보 표시함.

### 행동

* InputStreamResource를 사용한 **스트리밍 응답**으로 전환
* Content-Disposition/Content-Type/Content-Length 헤더를 정확히 세팅
* 파일 실체는 LocalBinaryContentStorag에서 InputStream으로 바로 읽어 전송

```java
// LocalBinaryContentStorage
@Override
  public InputStream getFile(Long binaryContentId) {
    Path filePath = findFileById(binaryContentId);
    if (filePath == null || Files.notExists(filePath)) {
      throw new NoSuchElementException("key가" + binaryContentId + "인 파일이 존재하지 않습니다.");
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
  
@Override
  public ResponseEntity<Resource> downloadFile(BinaryContentDTO metaData) {
    InputStream inputStream = getFile(metaData.id());
    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metaData.name() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size())).body(resource);
  }

// BinaryContentServiceImpl
@Override
  public ResponseEntity<Resource> downloadBinaryContent(Long id) {
    BinaryContentDTO dto = getBinaryContent(id);
    return storage.downloadFile(dto);
  }
```

### 결과

* 메모리 사용이 파일 크기 의존 -> 버퍼 크기 수준으로 감소, **OOM 방지**
* 다운로드 시작 지연 단축
* 브라우저 파일명 표시 가능

---

## 박종현
## 페이지네이션 구현 (ChangeLog)

### 상황

- 대량 데이터에서도 안정적인 페이지네이션 정렬 필요.

### 문제

- 페이지네이션을 사용하지 않으면 전체 쿼리를 항상 구해와 성능적으로 떨어짐
- ChangeLog, Histories같은 데이터는 많이 쌓이므로 페이지별로 구해오는 과정이 필요함.

### 행동

- Service
    - Repository의 검색 결과를 size + 1로 조회, hasNext 판별 후 자르기
    - 마지막 요소의 `id` 다음 커서로 사용: `nextCursor = {"id": <lastId>}` 문자열 생성

<details>
  <summary>searchChangeLogs 메서드 보기</summary>

```java
@Override
public CursorPageResponse<ChangeLogDTO> searchChangeLogs(
    String employeeNumber, String memo, String ipAddress, ChangeLogType type, Instant atFrom, Instant atTo, Long idAfter,
    String cursor, int size, String sortField, String sortDirection) {

  List<ChangeLog> changeLogs = changeLogRepository.searchChangeLogs(
      employeeNumber, memo, ipAddress, type, atFrom, atTo, idAfter, size + 1, sortField, sortDirection);
  boolean hasNext = changeLogs.size() > size;
  if(hasNext) changeLogs = changeLogs.subList(0, size);

  List<ChangeLogDTO> dtos = changeLogs.stream()
      .map(changeLogMapper::toDto)
      .toList();

  Long nextIdAfter = hasNext ? dtos.get(dtos.size() - 1).id() : null;
  long totalCount = changeLogRepository.countChangeLogs(employeeNumber, memo, ipAddress, type, atFrom, atTo);

  return CursorPageResponse.<ChangeLogDTO>builder()
      .content(dtos)
      .nextCursor(nextIdAfter != null ? ("{\"id\":" + nextIdAfter + "}") : null)
      .nextIdAfter(nextIdAfter)
      .size(size)
      .totalElements(totalCount)
      .hasNext(hasNext)
      .build();
}
```
</details> 

### 결과

- 운영자가 원하는 조건으로 안정적인 이력 조회 가능.
- 커서 방식으로 스크롤형 페이지네이션 시 응답 안정성 확보.
  
## 파일 구조
<details>
  <summary>파일구조 보기</summary>
 
```
├─src
  ├─main
  │  ├─java
  │  │  └─com
  │  │      └─codeit
  │  │          └─hrbank
  │  │              │  HrBankApplication.java
  │  │              │
  │  │              ├─config
  │  │              │      OpenApiConfig.java
  │  │              │      QuerydslConfig.java
  │  │              │      WebConfig.java
  │  │              │
  │  │              ├─controller
  │  │              │      BackupController.java
  │  │              │      BinaryContentController.java
  │  │              │      ChangeLogController.java
  │  │              │      DepartmentController.java
  │  │              │      DownloadController.java
  │  │              │      EmployeeController.java
  │  │              │
  │  │              ├─dto
  │  │              │  ├─data
  │  │              │  │      BackupDTO.java
  │  │              │  │      BinaryContentDTO.java
  │  │              │  │      ChangeLogDTO.java
  │  │              │  │      DepartmentDTO.java
  │  │              │  │      EmployeeDistributionDTO.java
  │  │              │  │      EmployeeDTO.java
  │  │              │  │      EmployeeTrendDTO.java
  │  │              │  │      HistoryDTO.java
  │  │              │  │
  │  │              │  ├─request
  │  │              │  │      BinaryContentCreateRequest.java
  │  │              │  │      BinaryContentUpdateRequest.java
  │  │              │  │      DepartmentCreateRequest.java
  │  │              │  │      DepartmentUpdateRequest.java
  │  │              │  │      EmployeeCreateRequest.java
  │  │              │  │      EmployeeUpdateRequest.java
  │  │              │  │      HistoryCreateRequest.java
  │  │              │  │      HistoryUpdateRequest.java
  │  │              │  │
  │  │              │  └─response
  │  │              │          CursorPageResponse.java
  │  │              │
  │  │              ├─entity
  │  │              │      Backup.java
  │  │              │      BackupStatus.java
  │  │              │      BinaryContent.java
  │  │              │      ChangeLog.java
  │  │              │      ChangeLogType.java
  │  │              │      DateUnit.java
  │  │              │      Department.java
  │  │              │      Employee.java
  │  │              │      EmployeeGroupBy.java
  │  │              │      EmployeeStatus.java
  │  │              │      History.java
  │  │              │
  │  │              ├─exception
  │  │              │      GlobalExceptionHandler.java
  │  │              │
  │  │              ├─mapper
  │  │              │      BackupMapper.java
  │  │              │      BinaryContentMapper.java
  │  │              │      ChangeLogMapper.java
  │  │              │      DepartmentMapper.java
  │  │              │      EmployeeMapper.java
  │  │              │      HistoryMapper.java
  │  │              │      PageResponseMapper.java
  │  │              │
  │  │              ├─repository
  │  │              │  │  BackupQueryRepository.java
  │  │              │  │  BackupRepository.java
  │  │              │  │  BinaryContentRepository.java
  │  │              │  │  ChangeLogQueryRepository.java
  │  │              │  │  ChangeLogRepository.java
  │  │              │  │  DepartmentQueryRepository.java
  │  │              │  │  DepartmentRepository.java
  │  │              │  │  EmployeeQueryRepository.java
  │  │              │  │  EmployeeRepository.java
  │  │              │  │  HistoryRepository.java
  │  │              │  │
  │  │              │  └─impl
  │  │              │          BackupQueryRepositoryImpl.java
  │  │              │          ChangeLogQueryRepositoryImpl.java
  │  │              │          DepartmentQueryRepositoryImpl.java
  │  │              │          EmployeeQueryRepositoryImpl.java
  │  │              │
  │  │              ├─scheduler
  │  │              │      MyScheduler.java
  │  │              │
  │  │              ├─service
  │  │              │  │  BackupService.java
  │  │              │  │  BinaryContentService.java
  │  │              │  │  ChangeLogService.java
  │  │              │  │  DepartmentService.java
  │  │              │  │  EmployeeAnalyticsService.java
  │  │              │  │  EmployeeService.java
  │  │              │  │
  │  │              │  ├─csv
  │  │              │  │      CsvExportService.java
  │  │              │  │
  │  │              │  └─impl
  │  │              │          BackupServiceImpl.java
  │  │              │          BinaryContentServiceImpl.java
  │  │              │          ChangeLogServiceImpl.java
  │  │              │          DepartmentServiceImpl.java
  │  │              │          EmployeeAnalyticsServiceImpl.java
  │  │              │          EmployeeServiceImpl.java
  │  │              │
  │  │              ├─storage
  │  │              │  │  BinaryContentStorage.java
  │  │              │  │
  │  │              │  └─type
  │  │              │          LocalBinaryContentStorage.java
  │  │              │
  │  │              └─util
  │  │                      ChangeLogUtils.java
  │  │
  │  └─resources
  │        application-dev.yml
  │        application-prod.yml
  │        application.yml
  │        schema.sql
  │      
  │      
  └─test
      └─java
          └─com
              └─codeit
                  └─hrbank
                          HrBankApplicationTests.java
```
</details>

--- 

## 이예림
## 대시보드_기간 단위별 직원 수 집계

### 상황

* 일/주/월/분기/년 단위로 버킷(기간 단위 묶음)을 만들어 각 구간의 직원수 추이를 계산해야 함.

### 문제

* 마지막 버킷의 끝 경계가 to를 넘거나 모자라서 하루가 중복/누락되는 사례 발생 -> 그래프가 한 칸 밀리거나 값이 과소/과대 집계됨

### 행동

* 마지막 버킷만 endExclusive = toDate.plusDays(1)로 보정하고, 스냅샷 기준일을 ref = endExclusive.minusDays(1)로 통일
  
  ```java
	LocalDate nextStart = bump(start, dateUnit);
	LocalDate endExclusive = (i + 1 < starts.size()) ? nextStart : toDate.plusDays(1);
	LocalDate ref = endExclusive.minusDays(1); // 구간 끝 하루
	counts.add(employeeRepository.countEmployeesAt(ref));
  ```

### 결과

* 월/분기의 초반과 말에 값이 튀던 현상 제거
* 그래프 안정성 ↑
  
---

## 대시보드_부서별 직원 분포 집계

### 상황

* 대시보드에 부서별 직원 분포를 보여줘야 함.

### 문제

* 부서별
	* 초기에는 각 부서마다 employeeRepository.countByDepartmentAndStatus() 쿼리를 따로 호출되는 N+1 문제 발생 
  	* 결과적으로 DB 부하 증가 및 응답 속도 느려짐


 ### 행동
 
* 부서별
  * 모든 부서 ID를 한 번에 모아서 단일 쿼리로 카운트 가져오도록 변경
  * ID 집합으로 모아 한번에 group by 조회

	```java
	Set<Long> deptIds = new HashSet<>();
	departments.forEach(d -> deptIds.add(d.getId()));
	Map<Long, Long> counts = employeeRepository.countEmployeesByDepartmentIds(status, deptIds);
	  ```

### 결과
* 쿼리 호출 수 1회로 단축
* 응답 속도 개션
* 부서 개수가 늘어나도 성능 안정적 유지
  
---

## 구현 홈페이지
- 실제 배포 사이트 : [https://hrbank-production.up.rail](https://hrbank-production.up.railway.app/swagger-ui/index.html)
- 배포 Swagger :  [Swagger](https://hrbank-production.up.railway.app/swagger-ui/index.html)

## 프로젝트 회고록
(제작한 발표자료 링크 혹은 첨부파일)
