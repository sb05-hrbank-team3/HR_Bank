package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.request.ChangeLogCreateRequest;
import com.codeit.hrbank.dto.response.ChangeLogCreateResponse;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.History;
import com.codeit.hrbank.repository.ChangeLogRepository;
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
//  private final HistoryRepository historyRepository;

  @Override
  @Transactional
  public ChangeLogCreateResponse createChangeLog(ChangeLogCreateRequest request, String ip) {
    Employee employee = null; //employee 생성

    Instant at = Instant.now();

    ChangeLog newLog = ChangeLog.builder()
        .type(request.type())
        .ipAddress(ip)
        .memo(request.memo())
        .at(at)
        .employee(employee)
        .build();
    changeLogRepository.save(newLog);

//    if (request.histories() != null && !request.histories().isEmpty()) {
//      List<History> histories = request.histories().stream()
//              .map(h -> History.builder()
//                  .propertyName(h.name())
//                  .before(h.before())
//                  .after(h.after())
//                  .changeLogs(newLog)
//                  .build())
//                  .toList();
//        historyRepository.saveAll(histories);
//      }

//    return new ChangeLogCreateResponse(, request.type(), , request.memo(), ip, at);
    return null;
  }
}
