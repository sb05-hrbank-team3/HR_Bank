package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.EmployeeStatus;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeQueryRepository {
  List<Employee> findAllQEmployeesPart(
      String nameOrEmail,
      String employeeNumber,
      String departmentName,
      String position,
      LocalDate hireDateFrom,
      LocalDate hireDateTo,
      EmployeeStatus status,
      Long idAfter,
      Integer size,
      String sortField,
      String sortDirection
  );

}
