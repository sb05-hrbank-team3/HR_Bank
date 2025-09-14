package com.codeit.hrbank.scheduler;


import com.codeit.hrbank.service.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class MyScheduler {
  private final BackupService backupService;


  @Scheduled(cron = "${hrbank.backup.cron}")
  public void runBackupJob() throws IOException {

    backupService.createBackupForScheduler();
  }

}
