package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.request.ChangeLogCreateRequest;
import com.codeit.hrbank.dto.response.ChangeLogCreateResponse;

public interface ChangeLogService {
  ChangeLogCreateResponse createChangeLog(ChangeLogCreateRequest request, String ip);
//  HistoryResponse changeLogDetails(Long id);
}
