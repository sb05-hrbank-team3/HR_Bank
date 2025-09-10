package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.QDepartment;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;


public interface DepartmentQueryRepository {
  List<Department> findAndSortDepartments(String nameOrDescription,
      Instant cursor,
      Long idAfter,
      int size,
      String sortField,
      String sortDirection);


}
