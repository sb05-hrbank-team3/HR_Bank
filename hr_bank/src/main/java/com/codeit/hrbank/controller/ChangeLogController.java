package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.ChangeLogType;
import com.codeit.hrbank.service.ChangeLogService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant atFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant atTo,
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

//  @GetMapping("/{id}/diffs")
//  public ResponseEntity<HistoryResponse> details(@PathVariable Long id) {
//    return ResponseEntity.ok(changeLogService.)
//  }


//  private String getClientIp(HttpServletRequest request) {
//    String ip = request.getHeader("X-Forwarded-For");
//    if (ip != null && !ip.isBlank()) {
//      // X-Forwarded-For: client1, proxy1, proxy2 ...
//      int comma = ip.indexOf(',');
//      return (comma > 0 ? ip.substring(0, comma) : ip).trim();
//    }
//
//    ip = request.getHeader("X-Real-IP");
//    if (ip != null && !ip.isBlank()) {
//      return ip.trim();
//    }
//
//    return request.getRemoteAddr();
//  }
}
