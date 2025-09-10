package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.Department;
import java.time.Instant;
import java.util.List;

public interface ChangeLogQueryRepository {
  ChangeLog findChangeLog();
}