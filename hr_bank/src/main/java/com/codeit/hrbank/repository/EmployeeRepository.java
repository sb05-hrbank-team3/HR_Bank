package com.codeit.hrbank.repository;


import com.codeit.hrbank.entity.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeQueryRepository {

  boolean existsByEmail(String email);

  boolean existsByEmailAndIdNot(String email, Long id);

  @Query("select e from Employee e " +
      "left join fetch e.department " +
      "left join fetch e.binaryContent " +
      "where e.id = :id")
  Optional<Employee> findByIdWithRelations(Long id);
}

