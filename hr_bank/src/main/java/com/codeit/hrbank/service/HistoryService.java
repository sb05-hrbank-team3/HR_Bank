package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.HistoryDTO;
import com.codeit.hrbank.dto.request.HistoryCreateRequest;
import com.codeit.hrbank.dto.request.HistoryUpdateRequest;
import java.util.List;

public interface HistoryService {
  HistoryDTO create(HistoryCreateRequest request);
  HistoryDTO find(Long id);
  List<HistoryDTO> findAllByChangeLogsId(Long logId);
  HistoryDTO update(Long id, HistoryUpdateRequest request);
  void delete(Long id);

}
