package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.request.ChangeLogCreateRequest;
import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.History;
import com.codeit.hrbank.mapper.ChangeLogMapper;
import com.codeit.hrbank.mapper.HistoryMapper;
import com.codeit.hrbank.repository.ChangeLogRepository;
import com.codeit.hrbank.repository.EmployeeRepository;
import com.codeit.hrbank.repository.HistoryRepository;
import com.codeit.hrbank.service.ChangeLogService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

  private final ChangeLogRepository changeLogRepository;
  private final HistoryRepository historyRepository;
  private final EmployeeRepository employeeRepository;
  private final ChangeLogMapper changeLogMapper;
  private final HistoryMapper historyMapper;

  @Override
  @Transactional
  public ChangeLogDTO createChangeLog(ChangeLogCreateRequest request, String ip) {
    Employee employee = employeeRepository.findByEmployeeNumber(request.employeeNumber());

    Instant at = Instant.now();
    ChangeLog newLog = changeLogMapper.toEntity(request, employee, ip, at);
    changeLogRepository.save(newLog);

    if (request.histories() != null && !request.histories().isEmpty()) {
      List<History> histories = request.histories().stream()
              .map(h -> historyMapper.toEntity(h, newLog))
                  .toList();
        historyRepository.saveAll(histories);
      }

    return new ChangeLogDTO(newLog.getId(), request.type(), request.employeeNumber(), request.memo(), ip, at, request.histories());
  }
}
