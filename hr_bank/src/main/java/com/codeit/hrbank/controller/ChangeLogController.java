package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.data.HistoryDTO;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.ChangeLogType;
import com.codeit.hrbank.service.ChangeLogService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class ChangeLogController {
  private final ChangeLogService changeLogService;

  @GetMapping
  public ResponseEntity<CursorPageResponse<ChangeLogDTO>> listChangeLog(
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) String memo,
      @RequestParam(required = false) String ipAddress,
      @RequestParam(required = false) ChangeLogType type,
      @RequestParam(required = false) Instant atFrom,
      @RequestParam(required = false) Instant atTo,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "at") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection
  ) {
    CursorPageResponse<ChangeLogDTO> response = changeLogService.searchChangeLogs(
        employeeNumber, memo, ipAddress, type, atFrom, atTo, idAfter, cursor, size, sortField, sortDirection);
    return ResponseEntity.ok(response);
  }

  @GetMapping(path = "/{id}/diffs")
  public ResponseEntity<List<HistoryDTO>> findDiffsByChangeLogId(@PathVariable Long id)
  {
    List<HistoryDTO> diffs = changeLogService.findAllByChangeLogsId(id);
    return ResponseEntity.ok(diffs);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> countChangeLog(
      @RequestParam(required = false) Instant fromDate,
      @RequestParam(required = false) Instant toDate) {

    Long count = changeLogService.countChangeLogBetween(fromDate, toDate);
    return ResponseEntity.ok(count);
  }
}
