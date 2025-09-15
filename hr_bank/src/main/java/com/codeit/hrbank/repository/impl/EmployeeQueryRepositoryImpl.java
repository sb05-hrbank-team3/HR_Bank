package com.codeit.hrbank.repository.impl;

import static com.codeit.hrbank.entity.QEmployee.employee;

import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.entity.QBinaryContent;
import com.codeit.hrbank.entity.QDepartment;
import com.codeit.hrbank.entity.QEmployee;
import com.codeit.hrbank.repository.EmployeeQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeQueryRepositoryImpl implements EmployeeQueryRepository {

  private final JPAQueryFactory queryFactory;
  private static final QEmployee employee = QEmployee.employee;
  private static final QDepartment department = QDepartment.department;
  private static final QBinaryContent binaryContent = QBinaryContent.binaryContent;

  @Override
  public List<Employee> findAllQEmployeesPart(
      String nameOrEmail,
      String employeeNumber,
      String departmentName,
      String position,
      LocalDate hireDateFrom,
      LocalDate hireDateTo,
      EmployeeStatus status,
      Long idAfter,
      String cursor,
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
    if (cursor != null && idAfter != null) {
      where.and(employee.name.gt(cursor)
          .or(employee.name.eq(cursor).and(employee.id.gt(idAfter)))
      );
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
        .leftJoin(employee.department, department).fetchJoin()
        .leftJoin(employee.binaryContent, binaryContent).fetchJoin()
        .where(where)
        .orderBy(order)
        .limit(size)
        .fetch();
  }

  @Override
  public Long countEmployeesByFilters(EmployeeStatus status, LocalDate hireDateFrom, LocalDate hireDateTo) {

    QEmployee employee = QEmployee.employee;
    BooleanBuilder where = new BooleanBuilder();

    if(status != null){
      where.and(employee.status.ne(EmployeeStatus.RESIGNED)); // 퇴시자 제외
    }
    if(hireDateFrom != null){
      where.and(employee.hireDate.goe(hireDateFrom));
    }
    if(hireDateTo != null){
      where.and(employee.hireDate.loe(hireDateTo));
    }
    return queryFactory
        .select(employee.count())
        .from(employee)
        .where(where)
        .fetchOne();
  }

  // 주어진 시점(at)에 재직/휴직 직원 수 카운트 (퇴사자 제외)
  @Override
  public Long countEmployeesAt(LocalDate at) {
    QEmployee employee = QEmployee.employee;
    return queryFactory
        .select(employee.count())
        .from(employee)
        .where(
            employee.hireDate.loe(at) // at 시점 이전 입사자
                .and(employee.status.ne(EmployeeStatus.RESIGNED)) // 퇴사자 제외
        )
        .fetchOne();
  }
  @Override
  public Long countEmployeesByStatus(EmployeeStatus status) {
    QEmployee employee = QEmployee.employee;
    return queryFactory
        .select(employee.count())
        .from(employee)
        .where(employee.status.eq(status))
        .fetchOne();
  }

  @Override
  public Map<Long, Long> countEmployeesByDepartmentIds(EmployeeStatus status,
      Set<Long> departmentIds) {
    if(departmentIds == null || departmentIds.isEmpty()) return Collections.emptyMap();

    QEmployee employee = QEmployee.employee;
    List<Tuple> rows = queryFactory.select(employee.department.id, employee.count())
        .from(employee)
        .where(
            employee.status.eq(status),
            employee.department.id.in(departmentIds)
        )
        .groupBy(employee.department.id)
        .fetch();

    return rows.stream().collect(Collectors.toMap(
        t -> t.get(0, Long.class),
        t -> t.get(1, Long.class)
    ));
  }

  @Override
  public List<Tuple> countEmployeesGroupByPosition(EmployeeStatus status) {
    QEmployee employee = QEmployee.employee;
    return queryFactory
        .select(employee.position, employee.count())
        .from(employee)
        .where(employee.status.eq(status))
        .groupBy(employee.position)
        .orderBy(employee.count().desc())
        .fetch();
  }

}
