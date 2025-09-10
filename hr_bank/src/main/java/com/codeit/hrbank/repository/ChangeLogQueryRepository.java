package com.codeit.hrbank.repository;

import com.codeit.hrbank.dto.request.ChangeLogSearchRequest;
import com.codeit.hrbank.entity.ChangeLog;
import java.util.List;

public interface ChangeLogQueryRepository {
  List<ChangeLog> searchChangeLogs(ChangeLogSearchRequest request);
  long countChangeLogs(ChangeLogSearchRequest request);
  ChangeLog findChangeLog();
}