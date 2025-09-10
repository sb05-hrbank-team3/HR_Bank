package com.codeit.hrbank.repository.impl;

import static com.codeit.hrbank.entity.QEmployee.employee;

import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.repository.EmployeeQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeQueryRepositoryImpl implements EmployeeQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Employee> findAllQEmployeesPart(
      String nameOrEmail,
      String employeeNumber,
      String departmentName,
      String position,
      Instant hireDateFrom,
      Instant hireDateTo,
      EmployeeStatus status,
      Long idAfter,
      Integer size,
      String sortField,
      String sortDirection
  ) {
    BooleanBuilder where = new BooleanBuilder();

    if (nameOrEmail != null && !nameOrEmail.isBlank()) {
      where.and(employee.name.containsIgnoreCase(nameOrEmail)
          .or(employee.email.containsIgnoreCase(nameOrEmail)));
    }
    if (employeeNumber != null && !employeeNumber.isBlank()) {
      where.and(employee.employeeNumber.containsIgnoreCase(employeeNumber));
    }
    if (departmentName != null && !departmentName.isBlank()) {
      where.and(employee.department.name.containsIgnoreCase(departmentName));
    }
    if (position != null && !position.isBlank()) {
      where.and(employee.position.containsIgnoreCase(position));
    }
    if (hireDateFrom != null) {
      where.and(employee.hireDate.goe(hireDateFrom));
    }
    if (hireDateTo != null) {
      where.and(employee.hireDate.loe(hireDateTo));
    }
    if (status != null) {
      where.and(employee.status.eq(status));
    }
    if (idAfter != null) {
      where.and(employee.id.gt(idAfter));
    }

    // 정렬
    com.querydsl.core.types.OrderSpecifier<?> order;
    boolean asc = !"DESC".equalsIgnoreCase(sortDirection);
    order = switch (sortField) {
      case "hireDate" -> asc ? employee.hireDate.asc() : employee.hireDate.desc();
      case "employeeNumber" -> asc ? employee.employeeNumber.asc() : employee.employeeNumber.desc();
      case "name" -> asc ? employee.name.asc() : employee.name.desc();
      default -> employee.id.asc();
    };

    return queryFactory.selectFrom(employee)
        .where(where)
        .orderBy(order)
        .limit(size)
        .fetch();
  }
}
