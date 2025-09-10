package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.request.ChangeLogCreateRequest;
import com.codeit.hrbank.dto.data.ChangeLogDTO;

public interface ChangeLogService {
  ChangeLogDTO createChangeLog(ChangeLogCreateRequest request, String ip);
//  HistoryResponse changeLogDetails(Long id);
}
