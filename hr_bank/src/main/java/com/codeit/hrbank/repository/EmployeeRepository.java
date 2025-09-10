package com.codeit.hrbank.repository;


import com.codeit.hrbank.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeQueryRepository {

  Employee findByEmployeeNumber(String employeeNumber);

  boolean existsByEmail(String email);

  boolean existsByEmailAndIdNot(String email, Long id);
}
