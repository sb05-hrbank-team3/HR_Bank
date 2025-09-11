package com.codeit.hrbank.controller;


import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@Tag(name = "부서 관리")
@RequestMapping("/api/departments")
public class DepartmentController {

  private final DepartmentService departmentService;


  @PostMapping
  public ResponseEntity<DepartmentDTO> create(@RequestBody DepartmentCreateRequest request) {

    DepartmentDTO departmentDTO = departmentService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(departmentDTO);

  }

  @GetMapping
  public ResponseEntity<CursorPageResponse<DepartmentDTO>> getAll(
      @RequestParam(required = false) String nameOrDescription,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "establishedDate") String sortField,
      @RequestParam(defaultValue = "asc") String sortDirection

  ) {
    CursorPageResponse<DepartmentDTO> department = departmentService.findAll(nameOrDescription,
        idAfter,
        cursor, size, sortField, sortDirection);

    return ResponseEntity.ok(department);
  }


  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDTO> findById(@PathVariable Long id) {

    return departmentService.findById(id)
        .map(ResponseEntity::ok)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable Long id) throws  IllegalStateException {
    departmentService.deleteById(id);
    return ResponseEntity.ok("부서가 삭제되었습니다.");

  }


  @PatchMapping("/{id}")
  public ResponseEntity<DepartmentDTO> update(@PathVariable Long id,
      @RequestBody DepartmentUpdateRequest request) {
    departmentService.update(id, request);
    return ResponseEntity.ok(null);
  }


}
