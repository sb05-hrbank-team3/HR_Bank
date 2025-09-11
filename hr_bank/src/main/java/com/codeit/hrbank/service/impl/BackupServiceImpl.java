package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.BackupDTO;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.Backup;
import com.codeit.hrbank.entity.BackupStatus;
import com.codeit.hrbank.entity.BinaryContent;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.mapper.BackupMapper;
import com.codeit.hrbank.mapper.PageResponseMapper;
import com.codeit.hrbank.repository.BackupRepository;
import com.codeit.hrbank.repository.ChangeLogRepository;
import com.codeit.hrbank.service.BackupService;
import com.codeit.hrbank.service.csv.CsvExportService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class BackupServiceImpl implements BackupService {

  private final BackupRepository backupRepository;
  private final ChangeLogRepository changeLogRepository;
  private final BackupMapper backupMapper;
  private final CsvExportService csvExportService;
  private final PageResponseMapper pageResponseMapper;


  @Override
  public CursorPageResponse<BackupDTO> findAllBackups(String worker, BackupStatus status,
      Instant startedAtFrom, Instant startedAtTo
      , Long idAfter, String cursor, int size, String sortField, String sortDirection) {
    List<Backup> backups = backupRepository.findAllBackups(worker, status, startedAtFrom,
        startedAtTo,
        idAfter, cursor, size, sortField, sortDirection);

    // hasNext 체크
    boolean hasNext = backups.size() > size;
    if (hasNext) {
      backups = backups.subList(0, size);
    }

    // DTO 변환
    List<BackupDTO> backupDTOs = backups.stream()
        .map(backupMapper::toDto)
        .toList();
    // 다음 커서 정보 설정
    Long nextIdAfter = null;
    String nextCursor = null;
    if (!backups.isEmpty()) {
      nextIdAfter = backups.get(backups.size() - 1).getId();
      nextCursor = String.valueOf(nextIdAfter);
    }


    return pageResponseMapper.fromCursor(backupDTOs, size, nextCursor, nextIdAfter, hasNext);
  }

  @Transactional
  public BackupDTO createBackup(HttpServletRequest request) throws IOException {
    //IP 확인하는 부분
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    // X-Forwarded-For 에는 "실제클라이언트IP, 프록시IP, ..." 식으로 여러 개가 들어올 수 있음
    if (ip != null && ip.contains(",")) {
      ip = ip.split(",")[0].trim();
    }

    Instant requestTime = Instant.now();

    List<Backup> backups = backupRepository.findAll();

    if (backups.isEmpty()) {
      BinaryContent bc = csvExportService.exportEmployeesToCsv();

      Backup backup = backupRepository.save(Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.COMPLETED)
          .file(bc)
          .build()
      );
      return backupMapper.toDto(backup);
    }

    ChangeLog changeLog = changeLogRepository.findChangeLog();

    if (changeLog == null) {
      Backup backup = backupRepository.save(Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.FAILED)
          .build()
      );

      return backupMapper.toDto(backup);
    }

    Instant lastBackup =backupRepository.getBackupLatest(BackupStatus.COMPLETED).getEndedAt();


    if (changeLog.getAt().isBefore(lastBackup)) {
      Backup backup = backupRepository.save(Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.SKIPPED)
          .build()
      );

      return backupMapper.toDto(backup);
    }

    if (changeLog.getAt().isAfter(lastBackup)) {
      BinaryContent bc = csvExportService.exportEmployeesToCsv();

      Backup backup = backupRepository.save(Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.COMPLETED)
          .file(bc)
          .build()
      );
      return backupMapper.toDto(backup);
    }

    return new BackupDTO(null, null, null, null, null, null);
  }


  //scheduler를 위해 파라미터가 없는 함수 추가
  @Transactional
  @Override
  public BackupDTO createBackupForScheduler() throws IOException {
    //내 컴퓨터의 IP
    InetAddress localHost = InetAddress.getLocalHost();
    String ip = localHost.getHostAddress();

    Instant requestTime = Instant.now();

    List<Backup> backups = backupRepository.findAll();

    if (backups.isEmpty()) {
      BinaryContent bc = csvExportService.exportEmployeesToCsv();

      Backup backup = backupRepository.save(Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.COMPLETED)
          .file(bc)
          .build()
      );
      return backupMapper.toDto(backup);
    }

    ChangeLog changeLog = changeLogRepository.findChangeLog();

    if (changeLog == null) {
      Backup backup = backupRepository.save(Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.FAILED)
          .build()
      );

      return backupMapper.toDto(backup);
    }

    Instant lastBackup =backupRepository.getBackupLatest(BackupStatus.COMPLETED).getEndedAt();


    if (changeLog.getAt().isBefore(lastBackup)) {
      Backup backup = backupRepository.save(Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.SKIPPED)
          .build()
      );

      return backupMapper.toDto(backup);
    }

    if (changeLog.getAt().isAfter(lastBackup)) {
      BinaryContent bc = csvExportService.exportEmployeesToCsv();

      Backup backup = backupRepository.save(Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.COMPLETED)
          .file(bc)
          .build()
      );
      return backupMapper.toDto(backup);
    }

    return new BackupDTO(null, null, null, null, null, null);
  }


  public BackupDTO findLatestBackup(BackupStatus status) {
    Backup backupLatest = backupRepository.getBackupLatest(status);

    return backupMapper.toDto(backupLatest);
  }

}
