package com.codeit.hrbank.repository;


import com.codeit.hrbank.dto.data.KeyCount;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.EmployeeStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeQueryRepository {

  Employee findByEmployeeNumber(String employeeNumber);

  boolean existsByEmail(String email);

  boolean existsByEmailAndIdNot(String email, Long id);

}

