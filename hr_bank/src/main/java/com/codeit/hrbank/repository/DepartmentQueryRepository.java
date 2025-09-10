package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.Department;
import java.util.List;
import java.util.Map;


public interface DepartmentQueryRepository {
  List<Department> findAndSortDepartments(String nameOrDescription,
      String cursor,
      Long idAfter,
      int size,
      String sortField,
      String sortDirection);

  Map<Long, Long> findEmployeeCountsByDepartmentIds(List<Long> deptIds);


}
