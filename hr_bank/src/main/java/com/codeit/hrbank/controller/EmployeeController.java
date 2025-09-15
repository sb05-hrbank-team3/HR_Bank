package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.dto.data.EmployeeDistributionDTO;
import com.codeit.hrbank.dto.data.EmployeeTrendDTO;
import com.codeit.hrbank.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.EmployeeGroupBy;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.service.EmployeeAnalyticsService;
import com.codeit.hrbank.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
@Tag(name = "직원 관리")
public class EmployeeController {

  private final EmployeeService employeeService;
  private final EmployeeAnalyticsService employeeAnalyticsService;

  @Operation(summary = "직원 생성", description = "새로운 직원을 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "직원 생성 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeDTO> createEmployee(
      @Parameter(description = "직원 생성 요청 DTO") @RequestPart EmployeeCreateRequest employee,
      @Parameter(description = "프로필 이미지 파일") @RequestPart(required = false) MultipartFile profile,
      HttpServletRequest servletRequest
  ) {
    String clientIp = servletRequest.getRemoteAddr();
    EmployeeDTO response = employeeService.createEmployee(employee, profile, clientIp);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "직원 조회", description = "직원 ID로 상세 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "직원 없음")
  })
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDTO> findEmployeeById(
      @Parameter(description = "조회할 직원 ID", example = "1") @PathVariable Long id
  ) {
    EmployeeDTO response = employeeService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(summary = "직원 목록 조회", description = "조건에 따라 직원 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "직원 없음"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")
  })
  @GetMapping
  public ResponseEntity<CursorPageResponse<EmployeeDTO>> findAllEmployees(
      @Parameter(description = "이름 또는 이메일 검색") @RequestParam(required = false) String nameOrEmail,
      @Parameter(description = "직원 번호 검색") @RequestParam(required = false) String employeeNumber,
      @Parameter(description = "부서 이름 검색") @RequestParam(required = false) String departmentName,
      @Parameter(description = "직급 검색") @RequestParam(required = false) String position,
      @Parameter(description = "입사일 시작") @RequestParam(required = false) LocalDate hireDateFrom,
      @Parameter(description = "입사일 종료") @RequestParam(required = false) LocalDate hireDateTo,
      @Parameter(description = "직원 상태") @RequestParam(required = false) EmployeeStatus status,
      @Parameter(description = "ID 이후 조회") @RequestParam(required = false) Long idAfter,
      @Parameter(description = "커서")@RequestParam(required = false) String cursor,  //나중에 넣을 것
      @Parameter(description = "페이지 크기") @RequestParam(required = false, defaultValue = "10") Integer size,
      @Parameter(description = "정렬 필드") @RequestParam(required = false, defaultValue = "name") String sortField,
      @Parameter(description = "정렬 방향") @RequestParam(required = false, defaultValue = "asc") String sortDirection
  ) {
    CursorPageResponse<EmployeeDTO> response = employeeService.findAllByPart(
        nameOrEmail, employeeNumber, departmentName, position, hireDateFrom, hireDateTo, status,
        idAfter,cursor, size, sortField, sortDirection);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(summary = "직원 수정", description = "직원 정보를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공"),
      @ApiResponse(responseCode = "404", description = "직원 없음"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")
  })
  @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeDTO> updateEmployee(
      @Parameter(description = "수정할 직원 ID", example = "1") @PathVariable("id") Long employeeId,
      @Parameter(description = "직원 수정 DTO") @RequestPart EmployeeUpdateRequest employee,
      @Parameter(description = "프로필 이미지 파일") @RequestPart(required = false) MultipartFile profile,
      HttpServletRequest servletRequest
  ) {
    String clientIp = servletRequest.getRemoteAddr();
    EmployeeDTO response = employeeService.updateEmployee(employeeId, employee, profile, clientIp);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(summary = "직원 삭제", description = "직원 정보를 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "삭제 성공"),
      @ApiResponse(responseCode = "404", description = "직원 없음")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteEmployee(
      @Parameter(description = "삭제할 직원 ID", example = "1") @PathVariable Long id,
      HttpServletRequest servletRequest
  ) {
    String clientIp = servletRequest.getRemoteAddr();
    Employee employee = employeeService.deleteEmployeeDoSaveLog(id, clientIp);
    employeeService.deleteEmployee(employee);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("삭제 성공");
  }

  @Operation(summary = "직원 수 조회", description = "조건에 따라 직원 수를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공")
  })
  @GetMapping("/count")
  public ResponseEntity<Long> countEmployees(
      @Parameter(description = "직원 상태") @RequestParam(required = false) EmployeeStatus status,
      @Parameter(description = "조회 시작일") @RequestParam(required = false) LocalDate fromDate,
      @Parameter(description = "조회 종료일") @RequestParam(required = false) LocalDate toDate
  ) {
    Long count = employeeService.countEmployees(status, fromDate, toDate);
    return ResponseEntity.status(HttpStatus.OK).body(count);
  }

  @Operation(summary = "직원 추세 통계", description = "기간별 직원 추세를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")
  })
  @GetMapping("/stats/trend")
  public ResponseEntity<List<EmployeeTrendDTO>> trend(
      @Parameter(description = "조회 시작일") @RequestParam(required = false) LocalDate from,
      @Parameter(description = "조회 종료일") @RequestParam(required = false) LocalDate to,
      @Parameter(description = "단위 (day, week, month)") @RequestParam(required = false, defaultValue = "month") String unit
  ) {
    List<EmployeeTrendDTO> response = employeeAnalyticsService.getTrend(from, to, unit,
        ZoneId.systemDefault());
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(summary = "직원 분포 통계", description = "그룹별 직원 분포를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")
  })
  @GetMapping("/stats/distribution")
  public ResponseEntity<List<EmployeeDistributionDTO>> distribution(
      @Parameter(description = "그룹 기준 (department, status, position)") @RequestParam(required = false, defaultValue = "department") String groupBy,
      @Parameter(description = "직원 상태 필터") @RequestParam(required = false, defaultValue = "ACTIVE") EmployeeStatus status
  ) {
    try {
      List<EmployeeDistributionDTO> response = employeeAnalyticsService.getDistribution(
          EmployeeGroupBy.fromString(groupBy), status);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 그룹 기준 값");
    }
  }
}
