package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.HistoryDTO;
import com.codeit.hrbank.service.HistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/change-logs")
@RequiredArgsConstructor
public class HistoryController {

  private final HistoryService historyService;

  @GetMapping(path = "/{id}/diffs")
  public ResponseEntity<List<HistoryDTO>> findDiffsByChangeLogId(@PathVariable Long id)
  {
    List<HistoryDTO> diffs = historyService.findAllByChangeLogsId(id);
    return ResponseEntity.ok(diffs);
  }

}
