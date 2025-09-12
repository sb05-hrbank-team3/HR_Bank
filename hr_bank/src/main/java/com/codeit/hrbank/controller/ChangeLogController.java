package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.data.HistoryDTO;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.ChangeLogType;
import com.codeit.hrbank.service.ChangeLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
@Tag(name = "직원 수정 이력")
public class ChangeLogController {

  private final ChangeLogService changeLogService;

  @Operation(
      summary = "변경 로그 목록 조회",
      description = "변경 로그를 조회하며, 직원 번호, 메모, IP, 타입, 기간 등으로 필터링 가능",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청")
      }
  )
  @GetMapping
  public ResponseEntity<CursorPageResponse<ChangeLogDTO>> listChangeLog(
      @RequestParam(required = false)
      @Schema(description = "직원 번호, 부분 검색 가능", example = "EMP-2025-75CD7652")
      String employeeNumber,

      @RequestParam(required = false)
      @Schema(description = "메모, 부분 검색 가능", example = "테스트 메모")
      String memo,

      @RequestParam(required = false)
      @Schema(description = "IP 주소, 부분 검색 가능", example = "0:0:0:0:0:0:0:1")
      String ipAddress,

      @RequestParam(required = false)
      @Schema(description = "변경 로그 타입")
      ChangeLogType type,

      @RequestParam(required = false)
      @Schema(description = "조회 시작 시각(ISO-8601)", example = "2025-09-12T00:00:00Z")
      Instant atFrom,

      @RequestParam(required = false)
      @Schema(description = "조회 종료 시각(ISO-8601)", example = "2025-09-12T23:59:59Z")
      Instant atTo,

      @RequestParam(required = false)
      @Schema(description = "이 ID 이후의 데이터 조회용 커서", example = "100")
      Long idAfter,

      @RequestParam(required = false)
      @Schema(description = "페이지네이션 커서", example = "eyJpZCI6MTIyfQ==")
      String cursor,

      @RequestParam(defaultValue = "10")
      @Schema(description = "한 페이지 조회 개수", example = "10")
      int size,

      @RequestParam(defaultValue = "at")
      @Schema(description = "정렬 기준 필드", allowableValues = {"at"}, example = "at")
      String sortField,

      @RequestParam(defaultValue = "desc")
      @Schema(description = "정렬 방향", allowableValues = {"asc", "desc"}, example = "desc")
      String sortDirection
  ) {
    CursorPageResponse<ChangeLogDTO> response = changeLogService.searchChangeLogs(
        employeeNumber, memo, ipAddress, type, atFrom, atTo, idAfter, cursor, size, sortField,
        sortDirection);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(
      summary = "변경 로그 상세 변경 내역 조회",
      description = "특정 변경 로그 ID의 변경 내역(diff)을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "404", description = "변경 로그 없음")
      }
  )
  @GetMapping(path = "/{id}/diffs")
  public ResponseEntity<List<HistoryDTO>> findDiffsByChangeLogId(
      @Parameter(description = "조회할 변경 로그 ID", example = "1")
      @PathVariable("id") Long ChangeLogId) {
    List<HistoryDTO> diffs = changeLogService.findAllByChangeLogsId(ChangeLogId);
    return ResponseEntity.status(HttpStatus.OK).body(diffs);
  }

  @Operation(
      summary = "변경 로그 건수 조회",
      description = "기간별 변경 로그 건수를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공")
      }
  )
  @GetMapping("/count")
  public ResponseEntity<Long> countChangeLog(
      @RequestParam(required = false)
      @Schema(description = "조회 시작 시각(ISO-8601)", example = "2025-09-05T00:00:00Z")
      Instant fromDate,

      @RequestParam(required = false)
      @Schema(description = "조회 종료 시각(ISO-8601)", example = "2025-09-12T23:59:59Z")
      Instant toDate) {

    Long count = changeLogService.countChangeLogBetween(fromDate, toDate);
    return ResponseEntity.status(HttpStatus.OK).body(count);
  }
}
