package com.codeit.hrbank.controller;


import com.codeit.hrbank.dto.data.BackupDTO;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.BackupStatus;
import com.codeit.hrbank.service.BackupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "백업 관리")
@RequestMapping("/api/backups")
public class BackupController {

  private final BackupService backupService;

  @Operation(
      summary = "백업 파일 전체 찾기",
      description = "백업 파일 목록을 조회합니다. 필요시 worker, 상태, 기간 등으로 필터링 가능",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청"),
          @ApiResponse(responseCode = "500", description = "서버 오류")
      }
  )
  @GetMapping
  public ResponseEntity<CursorPageResponse<BackupDTO>> findAllBackups(
      @RequestParam(required = false)
      @Schema(description = "작업자 이름, 부분 검색 가능", example = "홍길동") String worker,
      @RequestParam(required = false)
      @Schema(description = "백업 상태") BackupStatus status,
      @RequestParam(required = false)
      @Schema(description = "조회 시작 시각(ISO-8601 형식)", example = "2025-09-12T00:00:00Z")
      Instant startedAtFrom,

      @RequestParam(required = false)
      @Schema(description = "조회 종료 시각(ISO-8601 형식)", example = "2025-09-12T23:59:59Z")
      Instant startedAtTo,

      @RequestParam(required = false)
      @Schema(description = "이 ID 이후의 데이터 조회용 커서", example = "100")
      Long idAfter,

      @RequestParam(required = false)
      @Schema(description = "페이지네이션 커서", example = "eyJpZCI6MTIyfQ==")
      String cursor,

      @RequestParam(defaultValue = "10")
      @Schema(description = "한 페이지 조회 개수", example = "10")
      int size,

      @RequestParam(defaultValue = "startedAt")
      @Schema(description = "정렬 기준 필드", allowableValues = {"startedAt", "endedAt", "worker",
          "status"}, example = "startedAt")
      String sortField,

      @RequestParam(defaultValue = "DESC")
      @Schema(description = "정렬 방향", allowableValues = {"ASC", "DESC"}, example = "DESC")
      String sortDirection
  ) {
    CursorPageResponse<BackupDTO> allBackups = backupService.findAllBackups(worker, status,
        startedAtFrom, startedAtTo, idAfter, cursor, size, sortField, sortDirection);

    return ResponseEntity.status(HttpStatus.OK).body(allBackups);
  }

  @Operation(
      summary = "백업 생성",
      description = "새로운 백업을 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "백업 생성 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청"),
          @ApiResponse(responseCode = "500", description = "서버 오류")
      }
  )
  @PostMapping
  public ResponseEntity<BackupDTO> createBackup(
      @Parameter(description = "HTTP 요청 객체") HttpServletRequest request) throws IOException {
    BackupDTO backupDto = backupService.createBackup(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(backupDto);
  }

  @Operation(
      summary = "가장 최신 백업 조회",
      description = "가장 최근 생성된 백업을 상태별로 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "404", description = "백업 없음")
      }
  )
  @GetMapping("/latest")
  public ResponseEntity<BackupDTO> findLatestBackup(
      @RequestParam(defaultValue = "COMPLETED", required = false)
      @Schema(description = "백업 상태") BackupStatus status){
    BackupDTO backupDto = backupService.findLatestBackup(status);
    return ResponseEntity.status(HttpStatus.OK).body(backupDto);
  }


}
