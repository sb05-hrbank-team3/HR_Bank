package com.codeit.hrbank.dto.data;

import com.codeit.hrbank.entity.BackupStatus;
import java.time.Instant;

public class BackupDTO {
  Long id;
  String worker;
  Instant startedAt;
  Instant endedAt;
  BackupStatus status;
  Long fileId;


}
