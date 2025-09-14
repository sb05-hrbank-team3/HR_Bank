package com.codeit.hrbank.repository.impl;


import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.QDepartment;
import com.codeit.hrbank.entity.QEmployee;
import com.codeit.hrbank.repository.DepartmentQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DepartmentQueryRepositoryImpl implements DepartmentQueryRepository {

  private final JPAQueryFactory queryFactory;
  private static QDepartment department = QDepartment.department;
  private final QEmployee employee = QEmployee.employee;


  @Override
  public List<Department> findAndSortDepartments(String nameOrDescription, String cursor,
      Long idAfter, int size,
      String sortField, String sortDirection) {
    BooleanBuilder where = new BooleanBuilder();

    if (nameOrDescription != null && !nameOrDescription.isBlank()) {
      where.and(department.name.containsIgnoreCase(nameOrDescription)
          .or(department.description.containsIgnoreCase(nameOrDescription)));
    }

    //커서 관련
    if (idAfter != null) {
      where.and(department.id.gt(idAfter));
    }

    // OrderSpecifier 생성
    boolean asc = !"DESC".equalsIgnoreCase(sortDirection);
    com.querydsl.core.types.OrderSpecifier<?> order = switch (sortField) {
      case "name" -> asc ? department.name.asc() : department.name.desc();
      case "establishedDate" -> asc ? department.establishedDate.asc() : department.establishedDate.desc();
      default -> department.id.asc(); // 기본 정렬
    };

    return queryFactory
        .select(department)
        .from(department)
        .limit(size)
        .where(where)
        .orderBy(order)
        .fetch();
  }

  @Override
  public Map<Long, Long> findEmployeeCountsByDepartmentIds(List<Long> deptIds) {
    if (deptIds.isEmpty()) return Collections.emptyMap();

    return queryFactory
        .select(employee.department.id, employee.count())
        .from(employee)
        .where(employee.department.id.in(deptIds))
        .groupBy(employee.department.id)
        .fetch()
        .stream()
        .collect(Collectors.toMap(
            tuple -> tuple.get(employee.department.id),
            tuple -> {
              Long count = tuple.get(employee.count());
              return count != null ? count : 0L; // null이면 0으로
            }
        ));
  }
}
