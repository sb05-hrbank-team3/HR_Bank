package com.codeit.hrbank.repository.impl;


import com.codeit.hrbank.entity.Backup;
import com.codeit.hrbank.entity.BackupStatus;
import com.codeit.hrbank.entity.QBackup;
import com.codeit.hrbank.entity.QChangeLog;
import com.codeit.hrbank.repository.BackupQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BackupQueryRepositoryImpl implements BackupQueryRepository {
  private final JPAQueryFactory queryFactory;
  private static QBackup backup  = QBackup.backup;

  @Override
  public List<Backup> findAllBackups(
      String worker, BackupStatus status, Instant startedAtFrom, Instant startedAtTo
      , int idAfter, String cursor, int size, String sortField, String sortDirection
  ) {
    BooleanBuilder where = new BooleanBuilder();

    if(worker != null && !worker.isBlank()) {
      where.and(backup.worker.containsIgnoreCase(worker));
    }

    if(status != null) {
      where.and(backup.status.eq(status));
    }

    if(startedAtFrom != null) {
      where.and(backup.startedAt.goe(startedAtFrom));
    }

    if(startedAtTo != null) {
      where.and(backup.endedAt.loe(startedAtTo));
    }

    if(sortField.equals("startedAt")) {
      if(sortDirection.equals("ASC")) {
        return queryFactory
            .select(backup)
            .from(backup)
            .where(where)
            .limit(size)
            .orderBy(backup.startedAt.asc())
            .fetch();
      }
      else if(sortDirection.equals("DESC")) {
        return queryFactory
            .select(backup)
            .from(backup)
            .where(where)
            .limit(size)
            .orderBy(backup.startedAt.desc())
            .fetch();

      }
    }

    if(sortField.equals("endedAt")) {
      if(sortDirection.equals("ASC")) {
        return queryFactory
            .select(backup)
            .from(backup)
            .where(where)
            .limit(size)
            .orderBy(backup.endedAt.asc())
            .fetch();


      }
      else if(sortDirection.equals("DESC")) {
        return queryFactory
            .select(backup)
            .from(backup)
            .where(where)
            .limit(size)
            .orderBy(backup.endedAt.desc())
            .fetch();

      }
    }

    if(sortField.equals("status")) {
      if(sortDirection.equals("ASC")) {
        return queryFactory
            .select(backup)
            .from(backup)
            .where(where)
            .limit(size)
            .orderBy(backup.status.asc())
            .fetch();

      }
    }
    return queryFactory
        .select(backup)
        .from(backup)
        .where(where)
        .limit(size)
        .orderBy(backup.status.desc())
        .fetch();

  }

  @Override
  public Backup getBackupLatest(BackupStatus status) {

    BooleanBuilder where = new BooleanBuilder();
    where.and( backup.status.eq(status) );

    return queryFactory
        .select(backup)
        .from(backup)
        .where(where)
        .orderBy(backup.endedAt.desc())
        .fetchFirst();

  }
}
