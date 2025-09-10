package com.codeit.hrbank.dto.data;

import com.codeit.hrbank.entity.BackupStatus;
import java.time.Instant;
import lombok.Builder;

@Builder
public record BackupDTO (
  Long id,
  String worker,
  Instant startedAt,
  Instant endedAt,
  BackupStatus status,
  Long fileId
){ }
