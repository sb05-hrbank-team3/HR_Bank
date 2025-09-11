package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.data.HistoryDTO;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.ChangeLogType;
import com.codeit.hrbank.mapper.ChangeLogMapper;
import com.codeit.hrbank.mapper.HistoryMapper;
import com.codeit.hrbank.repository.ChangeLogRepository;
import com.codeit.hrbank.repository.HistoryRepository;
import com.codeit.hrbank.service.ChangeLogService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

  private final ChangeLogRepository changeLogRepository;
  private final HistoryRepository historyRepository;
  private final ChangeLogMapper changeLogMapper;
  private final HistoryMapper historyMapper;

  @Override
  public CursorPageResponse<ChangeLogDTO> searchChangeLogs(
      String employeeNumber, String memo, String ipAddress, ChangeLogType type, Instant atFrom, Instant atTo, Long idAfter,
      String cursor, int size, String sortField, String sortDirection) {
//    if (request.atFrom() != null && request.atTo() != null && request.atFrom().isAfter(request.atTo())) return null;  // throw

    List<ChangeLog> changeLogs = changeLogRepository.searchChangeLogs(
        employeeNumber, memo, ipAddress, type, atFrom, atTo, idAfter, size + 1, sortField, sortDirection);
    boolean hasNext = changeLogs.size() > size;
    if(hasNext) changeLogs = changeLogs.subList(0, size);

    List<ChangeLogDTO> dtos = changeLogs.stream()
        .map(changeLogMapper::toDto)
        .toList();

    Long nextIdAfter = hasNext ? dtos.get(dtos.size() - 1).id() : null;
    long totalCount = changeLogRepository.countChangeLogs(employeeNumber, memo, ipAddress, type, atFrom, atTo);

    return CursorPageResponse.<ChangeLogDTO>builder()
        .content(dtos)
        .nextCursor(nextIdAfter != null ? ("{\"id\":" + nextIdAfter + "}") : null)
        .nextIdAfter(nextIdAfter)
        .size(size)
        .totalElements(totalCount)
        .hasNext(hasNext)
        .build();
  }


  @Transactional(readOnly = true)
  @Override
  public List<HistoryDTO> findAllByChangeLogsId(Long logId) {
    return historyRepository.findAllByChangeLogsId(logId).stream()
        .map(historyMapper::toDTO)
        .toList();
  }

  @Override
  public Long countChangeLogBetween(Instant fromDate, Instant toDate) {
    Instant now = Instant.now();
    Instant from = (fromDate == null) ? now.minus(7, ChronoUnit.DAYS) : fromDate;
    Instant to = (toDate == null) ? now : toDate;

    return changeLogRepository.countBetween(from, to);
  }
}
