package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.request.ChangeLogSearchRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.mapper.ChangeLogMapper;
import com.codeit.hrbank.mapper.HistoryMapper;
import com.codeit.hrbank.repository.ChangeLogRepository;
import com.codeit.hrbank.repository.EmployeeRepository;
import com.codeit.hrbank.repository.HistoryRepository;
import com.codeit.hrbank.service.ChangeLogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

  private final ChangeLogRepository changeLogRepository;
  private final HistoryRepository historyRepository;
  private final EmployeeRepository employeeRepository;
  private final ChangeLogMapper changeLogMapper;
  private final HistoryMapper historyMapper;

  @Override
  public CursorPageResponse<ChangeLogDTO> searchChangeLogs(ChangeLogSearchRequest request) {
    if (request.atFrom() != null && request.atTo() != null && request.atFrom().isAfter(request.atTo())) return null;  // return throw로 바꿔줘야 함

    List<ChangeLog> changeLogs = changeLogRepository.searchChangeLogs(request);
    boolean hasNext = changeLogs.size() > request.size();
    if(hasNext) changeLogs = changeLogs.subList(0, request.size());

    List<ChangeLogDTO> dtos = changeLogs.stream()
        .map(changeLogMapper::toDto)
        .toList();

    Long nextIdAfter = hasNext ? dtos.get(dtos.size() - 1).id() : null;
    long totalCount = changeLogRepository.countChangeLogs(request);

    return CursorPageResponse.<ChangeLogDTO>builder()
        .content(dtos)
        .nextCursor(nextIdAfter != null ? ("{\"id\":" + nextIdAfter + "}") : null)
        .nextIdAfter(nextIdAfter)
        .size(request.size())
        .totalElements(totalCount)
        .hasNext(hasNext)
        .build();
  }
}
