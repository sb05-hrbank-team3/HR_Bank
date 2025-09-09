package com.codeit.hrbank.repository;


import com.codeit.hrbank.entity.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeQueryRepository {

  @Query("SELECT e.employeeNumber FROM Employee e " +
      "WHERE EXTRACT(YEAR FROM e.hireDate) = :year " +
      "ORDER BY e.employeeNumber DESC")
  List<String> findEmployeeNumbersByYear(@Param("year") int year);

  default String findLastEmployeeNumberByYear(int year) {
    List<String> numbers = findEmployeeNumbersByYear(year);
    return numbers.isEmpty() ? null : numbers.get(0);
  }
}
