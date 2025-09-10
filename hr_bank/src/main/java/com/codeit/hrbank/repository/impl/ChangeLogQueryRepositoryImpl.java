package com.codeit.hrbank.repository.impl;

import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.QChangeLog;
import com.codeit.hrbank.entity.QDepartment;
import com.codeit.hrbank.repository.ChangeLogQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChangeLogQueryRepositoryImpl implements ChangeLogQueryRepository {
  
  private final JPAQueryFactory queryFactory;
  private static QChangeLog changeLog = QChangeLog.changeLog;



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
