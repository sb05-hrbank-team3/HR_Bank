package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

  @Operation(
      summary = "부서 생성",
      description = "새로운 부서를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "201", description = "부서 생성 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청"),
          @ApiResponse(responseCode = "500", description = "서버 오류")
      }
  )
  @PostMapping
  public ResponseEntity<DepartmentDTO> createDepartment(
      @Parameter(description = "부서 생성 요청 DTO") @RequestBody DepartmentCreateRequest request
  ) {
    DepartmentDTO response = departmentService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(
      summary = "부서 목록 조회",
      description = "부서 목록을 조회하며 이름 또는 설명, ID 이후, 커서, 페이지 크기, 정렬 등으로 필터링 가능",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공")
      }
  )
  @GetMapping
  public ResponseEntity<CursorPageResponse<DepartmentDTO>> getDepartments(
      @Parameter(description = "이름 또는 설명으로 검색", example = "인사")
      @RequestParam(required = false) String nameOrDescription,

      @Parameter(description = "이 ID 이후의 데이터 조회용 커서", example = "100")
      @RequestParam(required = false) Long idAfter,

      @Parameter(description = "페이지네이션 커서", example = "eyJpZCI6MTIyfQ==")
      @RequestParam(required = false) String cursor,

      @Parameter(description = "한 페이지 조회 개수", example = "10")
      @RequestParam(defaultValue = "10") int size,

      @Parameter(description = "정렬 기준 필드", example = "establishedDate")
      @RequestParam(defaultValue = "establishedDate") String sortField,

      @Parameter(description = "정렬 방향", example = "asc")
      @RequestParam(defaultValue = "asc") String sortDirection
  ) {
    CursorPageResponse<DepartmentDTO> response = departmentService.findAll(nameOrDescription,
        idAfter, cursor, size, sortField, sortDirection);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(
      summary = "부서 상세 조회",
      description = "ID로 부서 상세 정보를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "404", description = "부서 없음")
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDTO> findDepartment(
      @Parameter(description = "조회할 부서 ID", example = "1")
      @PathVariable("id") Long departmentId
  ) {
    DepartmentDTO response = departmentService.findById(departmentId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(
      summary = "부서 삭제",
      description = "ID로 부서를 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "삭제 성공"),
          @ApiResponse(responseCode = "404", description = "부서 없음")
      }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteDepartment(
      @Parameter(description = "삭제할 부서 ID", example = "1")
      @PathVariable("id") Long departmentId
  ) {
    departmentService.deleteById(departmentId);
    return ResponseEntity.status(HttpStatus.OK).body("부서가 삭제되었습니다.");
  }

  @Operation(
      summary = "부서 수정",
      description = "ID로 부서를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "수정 성공"),
          @ApiResponse(responseCode = "404", description = "부서 없음")
      }
  )
  @PatchMapping("/{id}")
  public ResponseEntity<DepartmentDTO> updateDepartment(
      @Parameter(description = "수정할 부서 ID", example = "1")
      @PathVariable("id") Long departmentId,

      @Parameter(description = "부서 수정 요청 DTO")
      @RequestBody DepartmentUpdateRequest request
  ) {
    DepartmentDTO response = departmentService.update(departmentId, request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
