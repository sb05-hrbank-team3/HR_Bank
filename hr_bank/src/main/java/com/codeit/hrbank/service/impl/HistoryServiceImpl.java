package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.HistoryDTO;
import com.codeit.hrbank.dto.request.HistoryCreateRequest;
import com.codeit.hrbank.dto.request.HistoryUpdateRequest;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.History;
import com.codeit.hrbank.mapper.HistoryMapper;
import com.codeit.hrbank.repository.ChangeLogRepository;
import com.codeit.hrbank.repository.HistoryRepository;
import com.codeit.hrbank.service.HistoryService;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

  private final HistoryRepository historyRepository;
  private final ChangeLogRepository changeLogRepository;
  private final HistoryMapper historyMapper;

  @Transactional
  @Override
  public HistoryDTO create(Long changeLogId, HistoryCreateRequest request) {

    ChangeLog changeLog = changeLogRepository.findById(changeLogId)
        .orElseThrow(() -> new NoSuchElementException("Change log with id "+ changeLogId+  " not found"));

    History history = historyMapper.toEntity(request, changeLog);
    historyRepository.save(history);

    return historyMapper.toDTO(history);
  }

  @Transactional(readOnly = true)
  @Override
  public HistoryDTO find(Long id) {
    History history = historyRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("History with id "+ id+  " not found"));

    return historyMapper.toDTO(history);
  }

  @Transactional(readOnly = true)
  @Override
  public List<HistoryDTO> findAllByChangeLogsId(Long logId) {
    return historyRepository.findAllByChangeLogsId(logId).stream()
        .map(historyMapper::toDTO)
        .toList();
  }

  @Transactional
  @Override
  public HistoryDTO update(Long id, HistoryUpdateRequest request) {
    History history = historyRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("History with id "+ id+  " not found"));

    historyMapper.updateEntityFromRequest(request, history);
    return historyMapper.toDTO(history);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    historyRepository.deleteById(id);
  }
}
