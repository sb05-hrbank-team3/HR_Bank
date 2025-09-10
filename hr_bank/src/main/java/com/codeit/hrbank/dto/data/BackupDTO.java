package com.codeit.hrbank.dto.data;

import com.codeit.hrbank.entity.BackupStatus;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record BackupDTO (
  Long id,
  String worker,
  LocalDate startedAt,
  LocalDate endedAt,
  BackupStatus status,
  Long fileId
){ }
