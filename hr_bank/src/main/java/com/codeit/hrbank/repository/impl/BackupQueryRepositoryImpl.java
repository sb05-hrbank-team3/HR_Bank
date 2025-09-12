package com.codeit.hrbank.repository.impl;


import com.codeit.hrbank.entity.Backup;
import com.codeit.hrbank.entity.BackupStatus;
import com.codeit.hrbank.entity.QBackup;
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
  private static final QBackup backup = QBackup.backup;

  @Override
  public List<Backup> findAllBackups(
      String worker,
      BackupStatus status,
      Instant startedAtFrom,
      Instant startedAtTo,
      Long idAfter,
      String cursor,
      int size,
      String sortField,
      String sortDirection
  ) {
    BooleanBuilder where = new BooleanBuilder();

    if (worker != null && !worker.isBlank()) {
      where.and(backup.worker.containsIgnoreCase(worker));
    }
    if (status != null) {
      where.and(backup.status.eq(status));
    }
    if (startedAtFrom != null) {
      where.and(backup.startedAt.goe(startedAtFrom));
    }
    if (startedAtTo != null) {
      where.and(backup.endedAt.loe(startedAtTo));
    }
    if (idAfter != null) {
      where.and(backup.id.gt(idAfter));
    }

    // 정렬 조건
    boolean asc = !"DESC".equalsIgnoreCase(sortDirection);
    com.querydsl.core.types.OrderSpecifier<?> order = switch (sortField) {
      case "startedAt" -> asc ? backup.startedAt.asc() : backup.startedAt.desc();
      case "endedAt" -> asc ? backup.endedAt.asc() : backup.endedAt.desc();
      case "status" -> asc ? backup.status.asc() : backup.status.desc();
      case "worker" -> asc ? backup.worker.asc() : backup.worker.desc();
      default -> backup.id.asc(); // 기본 정렬
    };

    return queryFactory
        .selectFrom(backup)
        .where(where)
        .orderBy(order)
        .limit(size + 1) // hasNext 판단 위해 +1
        .fetch();
  }


  @Override
  public Backup getBackupLatest(BackupStatus status) {

    BooleanBuilder where = new BooleanBuilder();
    where.and(backup.status.eq(status));

    return queryFactory
        .select(backup)
        .from(backup)
        .where(where)
        .orderBy(backup.endedAt.desc())
        .fetchFirst();

  }
}
