package com.codeit.hrbank.controller;


import com.codeit.hrbank.dto.data.BackupDTO;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.service.BackupService;
import com.codeit.hrbank.service.csv.CsvExportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/backups")
@Tag(name = "백업 관리")
public class BackupController {
  private final BackupService backupService;


  @GetMapping("")
  public ResponseEntity<BackupDTO> createBackup(HttpServletRequest request) throws IOException {
     BackupDTO backupDto = backupService.createBackup(request);


    return ResponseEntity.status(HttpStatus.OK).body(backupDto);
  }



}
