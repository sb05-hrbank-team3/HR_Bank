package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.ChangeLogType;
import java.time.Instant;
import java.util.List;

public interface ChangeLogQueryRepository {
  List<ChangeLog> searchChangeLogs(
      String employeeNumber, String memo, String ipAddress, ChangeLogType type, Instant atFrom, Instant atTo, Long idAfter,
      int limitPlusOne, String sortField, String sortDirection);
  Long countChangeLogs(
      String employeeNumber, String memo, String ipAddress, ChangeLogType type, Instant atFrom, Instant atTo);
  ChangeLog findChangeLog();
}