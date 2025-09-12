package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> , DepartmentQueryRepository{

  boolean existsByName(String name);
}
