package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.request.ChangeLogCreateRequest;
import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.request.ChangeLogSearchRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;

public interface ChangeLogService {
  ChangeLogDTO createChangeLog(ChangeLogCreateRequest request, String ip);

  CursorPageResponse<ChangeLogDTO> searchChangeLogs(ChangeLogSearchRequest request);
}
