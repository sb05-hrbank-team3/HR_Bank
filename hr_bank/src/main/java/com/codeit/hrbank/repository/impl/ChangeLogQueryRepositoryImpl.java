package com.codeit.hrbank.repository.impl;

import static com.codeit.hrbank.entity.QEmployee.employee;
import static com.codeit.hrbank.entity.QChangeLog.changeLog;

import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.ChangeLogType;
import com.codeit.hrbank.repository.ChangeLogQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChangeLogQueryRepositoryImpl implements ChangeLogQueryRepository {
  
  private final JPAQueryFactory queryFactory;

  @Override
  public List<ChangeLog> searchChangeLogs(
      String employeeNumber, String memo, String ipAddress, ChangeLogType type, Instant atFrom, Instant atTo, Long idAfter,
      int limitPlusOne, String sortField, String sortDirection) {
    BooleanBuilder where = new BooleanBuilder();

    if (employeeNumber != null && !employeeNumber.isBlank()) {
      where.and(employee.employeeNumber.contains(employeeNumber));
    }
    if (memo != null && !memo.isBlank()) {
      where.and(changeLog.memo.contains(memo));
    }
    if (ipAddress != null && !ipAddress.isBlank()) {
      where.and(changeLog.ipAddress.contains(ipAddress));
    }
    if (type != null) {
      where.and(changeLog.type.eq(type));
    }
    if (atFrom != null) {
      where.and(changeLog.at.goe(atFrom));
    }
    if (atTo != null) {
      where.and(changeLog.at.loe(atTo));
    }
    if (idAfter != null) {
      where.and(changeLog.id.gt(idAfter));
    }

    Order order = "asc".equalsIgnoreCase(sortDirection) ? Order.ASC : Order.DESC;
    OrderSpecifier<?> orderSpecifier =
        "ipAddress".equalsIgnoreCase(sortField) ?
            new OrderSpecifier<>(order, changeLog.ipAddress) :
            new OrderSpecifier<>(order, changeLog.at);

    return queryFactory
        .selectFrom(changeLog)
        .join(changeLog.employee, employee).fetchJoin()
        .where(where)
        .orderBy(orderSpecifier)
        .limit(limitPlusOne)
        .fetch();
  }

  @Override
  public Long countChangeLogs(String employeeNumber, String memo, String ipAddress, ChangeLogType type, Instant atFrom, Instant atTo) {
    BooleanBuilder where = new BooleanBuilder();
    if (employeeNumber != null && !employeeNumber.isBlank()) {
      where.and(employee.employeeNumber.contains(employeeNumber));
    }
    if (memo != null && !memo.isBlank()) {
      where.and(changeLog.memo.contains(memo));
    }
    if (ipAddress != null && !ipAddress.isBlank()) {
      where.and(changeLog.ipAddress.contains(ipAddress));
    }
    if (type != null) {
      where.and(changeLog.type.eq(type));
    }
    if (atFrom != null) {
      where.and(changeLog.at.goe(atFrom));
    }
    if (atTo != null) {
      where.and(changeLog.at.loe(atTo));
    }

    return queryFactory
        .select(changeLog.count())
        .from(changeLog)
        .join(changeLog.employee, employee)
        .where(where)
        .fetchOne();
  }

  @Override
  public ChangeLog findChangeLog() {
    BooleanBuilder where = new BooleanBuilder();
    where.and( changeLog.memo.containsIgnoreCase("직원") );

    return queryFactory
        .select(changeLog)
        .from(changeLog)
        .where(where)
        .orderBy(changeLog.at.desc())
        .fetchFirst();

  }
}
