package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
@Tag(name = "직원 관리")
public class EmployeeController {

  private final EmployeeService employeeService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeDTO> createEmployee(@RequestPart EmployeeCreateRequest employee,
      @RequestPart(required = false) MultipartFile profile) {
    EmployeeDTO entity = employeeService.createEmployee(employee, profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(entity); // 201 Created
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDTO> findEmployeeById(@PathVariable Long id) {
    EmployeeDTO employee = employeeService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(employee);
  }

  @GetMapping
  public ResponseEntity<CursorPageResponse<EmployeeDTO>> findAllEmployees(
      @RequestParam(required = false) String nameOrEmail,
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) String departmentName,
      @RequestParam(required = false) String position,
      @RequestParam(required = false)
      @DateTimeFormat(iso = ISO.DATE) LocalDate hireDateFrom,
      @RequestParam(required = false)
      @DateTimeFormat(iso = ISO.DATE) LocalDate hireDateTo,
      @RequestParam(required = false) EmployeeStatus status,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,    // 일단 보류 cursor
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "name") String sortField,
      @RequestParam(required = false, defaultValue = "asc") String sortDirection
  ) {
    // Swagger 이상꼼수 별 문제없으면 이렇게 진행
    Instant hireFrom = hireDateFrom != null ? hireDateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;
    Instant hireTo = hireDateTo != null ? hireDateTo.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;

    CursorPageResponse<EmployeeDTO> response = employeeService.findAllByPart(
        nameOrEmail, employeeNumber, departmentName, position, hireFrom, hireTo, status,
        idAfter, size, sortField, sortDirection);

    return ResponseEntity.ok(response);
  }

  @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeDTO> updateEmployee(
      @PathVariable("id") Long employeeId,
      @RequestPart EmployeeUpdateRequest employee,
      @RequestPart(required = false) MultipartFile profile) {
    EmployeeDTO entity = employeeService.updateEmployee(employeeId, employee, profile);
    return ResponseEntity.status(HttpStatus.OK).body(entity);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("삭제 성공");
  }
}
