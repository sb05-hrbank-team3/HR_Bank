package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.request.ChangeLogSearchRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;

public interface ChangeLogService {
  CursorPageResponse<ChangeLogDTO> searchChangeLogs(ChangeLogSearchRequest request);
}
