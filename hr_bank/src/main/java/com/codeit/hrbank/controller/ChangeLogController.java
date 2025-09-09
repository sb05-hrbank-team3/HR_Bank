package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.request.ChangeLogCreateRequest;
import com.codeit.hrbank.dto.response.ChangeLogCreateResponse;
import com.codeit.hrbank.service.ChangeLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class ChangeLogController {
  private final ChangeLogService changeLogService;

  @PostMapping
  public ResponseEntity<ChangeLogCreateResponse> createChangeLog(@RequestBody ChangeLogCreateRequest changeLogCreateRequest, HttpServletRequest httpServletRequest) {
    ChangeLogCreateResponse response = changeLogService.createChangeLog(changeLogCreateRequest, getClientIp(httpServletRequest));
    return ResponseEntity.ok(response);
  }

//  @GetMapping
//  public ResponseEntity<ChangeLogListResponse> listChangeLog(
//
//  ) {
//
//  }

//  @GetMapping("/{id}/diffs")
//  public ResponseEntity<HistoryResponse> details(@PathVariable Long id) {
//    return ResponseEntity.ok(changeLogService.)
//  }


  private String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip != null && !ip.isBlank()) {
      // X-Forwarded-For: client1, proxy1, proxy2 ...
      int comma = ip.indexOf(',');
      return (comma > 0 ? ip.substring(0, comma) : ip).trim();
    }

    ip = request.getHeader("X-Real-IP");
    if (ip != null && !ip.isBlank()) {
      return ip.trim();
    }

    return request.getRemoteAddr();
  }
}
