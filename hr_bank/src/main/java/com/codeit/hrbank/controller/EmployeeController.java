package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.CursorPageResponse;
import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/v1/employees")
@Tag(name = "직원 관리")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestPart EmployeeCreateRequest request,
        @RequestPart(required = false)MultipartFile profile) {
        EmployeeDTO employee = employeeService.createEmployee(request, profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee); // 201 Created
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
        @RequestParam(required = false) Instant hireDateFrom,
        @RequestParam(required = false) Instant hireDateTo,
        @RequestParam(required = false) String status,
        @PageableDefault(
            // size = 10, page = 0 default
            sort = "hireDate",
            direction = Direction.ASC
        )Pageable pageable) {

        CursorPageResponse<EmployeeDTO> response = employeeService.findAllByPart(
            nameOrEmail, employeeNumber, departmentName, position, hireDateFrom, hireDateTo, status, pageable);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeDTO> updateEmployee(
        @PathVariable("id") Long employeeId,
        @RequestPart EmployeeUpdateRequest request,
        @RequestPart(required = false) MultipartFile profile) {
        EmployeeDTO employee = employeeService.updateEmployee(employeeId, request, profile);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("삭제 성공");
    }
}
