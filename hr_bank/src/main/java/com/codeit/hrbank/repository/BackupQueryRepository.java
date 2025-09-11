package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.Backup;
import com.codeit.hrbank.entity.BackupStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

public interface BackupQueryRepository {
  List<Backup> findAllBackups(
      String worker,
      BackupStatus status,
      Instant startedAtFrom,
      Instant startedAtTo,
      int idAfter,
      String cursor,
      int size,
      String sortField,
      String sortDirection
  );

  Backup getBackupLatest(BackupStatus status);
}
