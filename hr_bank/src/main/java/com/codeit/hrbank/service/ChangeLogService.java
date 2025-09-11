package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.ChangeLogType;
import java.time.Instant;

public interface ChangeLogService {
  CursorPageResponse<ChangeLogDTO> searchChangeLogs(String employeeNumber, String memo,
      String ipAddress, ChangeLogType type, Instant atFrom, Instant atTo, Long idAfter,
      String cursor, int size, String sortField, String sortDirection);
}
