package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.BackupDTO;
import com.codeit.hrbank.entity.Backup;
import com.codeit.hrbank.entity.BackupStatus;
import com.codeit.hrbank.entity.BinaryContent;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.mapper.BackupMapper;
import com.codeit.hrbank.repository.BackupRepository;
import com.codeit.hrbank.repository.ChangeLogRepository;
import com.codeit.hrbank.service.BackupService;
import com.codeit.hrbank.service.csv.CsvExportService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

  private final BackupRepository backupRepository;
  private final ChangeLogRepository changeLogRepository;
  private final BackupMapper backupMapper;
  private final CsvExportService csvExportService;


  public BackupDTO createBackup(HttpServletRequest request) throws IOException {
    ChangeLog changeLog=changeLogRepository.findChangeLog();
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
    
    Instant requestTime=Instant.now();

    if(changeLog==null){
      Backup backup=backupRepository.save( Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.FAILED)
          .build()
      );

      return backupMapper.toDto(backup);
    }

    if(changeLog.getAt().isBefore(requestTime)){
      Backup backup=backupRepository.save( Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.SKIPPED)
          .build()
      );

      return backupMapper.toDto(backup);
    }


    if(changeLog.getAt().isAfter(requestTime)){
      BinaryContent bc=csvExportService.exportEmployeesToCsv();

      Backup backup=backupRepository.save( Backup.builder()
          .worker(ip)
          .startedAt(requestTime)
          .endedAt(Instant.now())
          .status(BackupStatus.COMPLETED)
          .file(bc)
          .build()
      );
      return backupMapper.toDto(backup);
    }

    return new BackupDTO(null,null,null,null,null,null);
  };


//  BackupDTO findLatestBackup(HttpServletRequest request , BackupStatus status){
//
//
//  };

}
