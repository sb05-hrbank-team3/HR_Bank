package com.codeit.hrbank.controller;


import com.codeit.hrbank.dto.data.BackupDTO;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.BackupStatus;
import com.codeit.hrbank.service.BackupService;
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
@RequestMapping("/api/backups")
@Tag(name = "백업 관리")
public class BackupController {
  private final BackupService backupService;

  @GetMapping
  public ResponseEntity<CursorPageResponse<BackupDTO>> findAllBackups(
      @RequestParam(required = false) String worker,
      @RequestParam(required = false) BackupStatus status ,
      @RequestParam(required = false) Instant startedAtFrom,
      @RequestParam(required = false) Instant startedAtTo,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "startedAt") String sortField,
      @RequestParam(defaultValue = "DESC") String sortDirection

      ){
    CursorPageResponse<BackupDTO> allBackups = backupService.findAllBackups(worker, status,
        startedAtFrom, startedAtTo
        , idAfter, cursor, size, sortField, sortDirection);

    return ResponseEntity.ok(allBackups);
  }

  @PostMapping
  public ResponseEntity<BackupDTO> createBackup(HttpServletRequest request) throws IOException {
     BackupDTO backupDto = backupService.createBackup(request);


    return ResponseEntity.status(HttpStatus.OK).body(backupDto);
  }

  @GetMapping("/latest")
  public ResponseEntity<BackupDTO> findLatestBackup(
      @RequestParam(defaultValue = "COMPLETED", required = false)BackupStatus status,
      HttpServletRequest request) {
    BackupDTO backupDto = backupService.findLatestBackup(status);

    return ResponseEntity.status(HttpStatus.OK).body(backupDto);
  }



}
