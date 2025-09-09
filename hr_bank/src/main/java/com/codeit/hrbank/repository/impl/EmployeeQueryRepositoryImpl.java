package com.codeit.hrbank.repository.impl;

import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.entity.QEmployee;
import com.codeit.hrbank.repository.EmployeeQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeQueryRepositoryImpl implements EmployeeQueryRepository {

  private final JPAQueryFactory queryFactory;

  private static final QEmployee e = QEmployee.employee;

  @Override
  public List<EmployeeDTO> findAllQEmployeesPart(
      String nameOrEmail,
      String employeeNumber,
      String departmentName,
      String position,
      Instant hireDateFrom,
      Instant hireDateTo,
      EmployeeStatus status,
      Pageable pageable) {

    var query = queryFactory
        .select(Projections.constructor(EmployeeDTO.class,
            e.id,
            e.name,
            e.email,
            e.employeeNumber,
            e.position
        ))
        .from(e);

    // 조건 추가
    if (nameOrEmail != null && !nameOrEmail.isEmpty()) {
      query.where(e.name.containsIgnoreCase(nameOrEmail)
          .or(e.email.containsIgnoreCase(nameOrEmail)));
    }

    if (employeeNumber != null && !employeeNumber.isEmpty()) {
      query.where(e.employeeNumber.eq(employeeNumber));
    }

    if (departmentName != null && !departmentName.isEmpty()) {
      query.where(e.department.name.eq(departmentName));
    }

    if (position != null && !position.isEmpty()) {
      query.where(e.position.eq(position));
    }

    if (hireDateFrom != null) {
      query.where(e.hireDate.goe(hireDateFrom));
    }

    if (hireDateTo != null) {
      query.where(e.hireDate.loe(hireDateTo));
    }

    if (status != null) {
      query.where(e.status.eq(status));
    }

    // 페이징 적용
    query.offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    return query.fetch();
  }
}
