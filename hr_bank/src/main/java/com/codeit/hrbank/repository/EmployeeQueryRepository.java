package com.codeit.hrbank.repository;

import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.entity.EmployeeStatus;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface EmployeeQueryRepository {
  List<EmployeeDTO> findAllQEmployeesPart(
      String nameOrEmail,
      String employeeNumber,
      String departmentName,
      String position,
      Instant hireDateFrom,
      Instant hireDateTo,
      EmployeeStatus status,
      Pageable pageable);

}
