package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.request.ChangeLogSearchRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.service.ChangeLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class ChangeLogController {
  private final ChangeLogService changeLogService;

  @GetMapping
  public ResponseEntity<CursorPageResponse<ChangeLogDTO>> listChangeLog(
      ChangeLogSearchRequest request
  ) {
    CursorPageResponse<ChangeLogDTO> response = changeLogService.searchChangeLogs(request);
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
