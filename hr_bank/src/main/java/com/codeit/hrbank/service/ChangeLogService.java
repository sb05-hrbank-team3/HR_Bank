package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.request.ChangeLogSearchRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import java.time.Instant;

public interface ChangeLogService {
  CursorPageResponse<ChangeLogDTO> searchChangeLogs(ChangeLogSearchRequest request);
}
