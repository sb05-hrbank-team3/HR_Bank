package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.BackupDTO;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.BackupStatus;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;

public interface BackupService {

  CursorPageResponse<BackupDTO> findAllBackups(String worker, BackupStatus status,
      Instant startedAtFrom, Instant startedAtTo
      , Long idAfter, String cursor, int size, String sortField, String sortDirection);


  BackupDTO createBackup(HttpServletRequest request) throws IOException;

  void createBackupForScheduler() throws IOException;

  BackupDTO findLatestBackup(BackupStatus status);
}
