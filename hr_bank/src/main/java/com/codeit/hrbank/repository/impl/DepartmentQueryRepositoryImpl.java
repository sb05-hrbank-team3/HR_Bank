package com.codeit.hrbank.repository.impl;


import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.QDepartment;
import com.codeit.hrbank.repository.DepartmentQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DepartmentQueryRepositoryImpl implements DepartmentQueryRepository {

  private final JPAQueryFactory queryFactory;
  private static QDepartment department = QDepartment.department;


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

    if (sortField.equals("name") && sortDirection.equals("asc")) {
      return queryFactory
          .select(department)
          .from(department)
          .limit(size)
          .where(where)
          .orderBy(department.name.asc())
          .fetch();
    }

    if (sortField.equals("name") && sortDirection.equals("desc")) {
      return queryFactory
          .select(department)
          .from(department)
          .limit(size)
          .where(where)
          .orderBy(department.name.desc())
          .fetch();
    }

    if (sortField.equals("establishedDate") && sortDirection.equals("desc")) {
      return queryFactory
          .select(department)
          .from(department)
          .limit(size)
          .where(where)
          .orderBy(department.establishedDate.desc())
          .fetch();
    }

    return queryFactory
        .select(department)
        .from(department)
        .limit(size)
        .where(where)
        .orderBy(department.establishedDate.asc())
        .fetch();
  }
}
