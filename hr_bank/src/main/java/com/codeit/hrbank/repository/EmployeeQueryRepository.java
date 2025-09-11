package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.querydsl.core.Tuple;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

  Long countEmployeesByFilters(
      EmployeeStatus status,
      LocalDate hireDateFrom,
      LocalDate hireDateTo)
      ;

  Long countEmployeesAt(LocalDate at);

  // 상태별 전체 직원 수
  Long countEmployeesByStatus(EmployeeStatus status);

  // 부서 id 그룹에 대한 상태별 직원수 그룹핑
  Map<Long, Long> countEmployeesByDepartmentIds(EmployeeStatus status, Set<Long> departmentIds);

  // 직무별 분포
  List<Tuple> countEmployeesGroupByPosition(EmployeeStatus status);
}
