package com.codeit.hrbank.scheduler;


import com.codeit.hrbank.service.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class MyScheduler {
  private final BackupService backupService;

  private final JobLauncher jobLauncher;
  private final Job csvExportJob;

  @Scheduled(cron = "${hrbank.backup.cron}")
  public void runBackupJob()
      throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    JobParameters params = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
            .toJobParameters();

    jobLauncher.run(csvExportJob,params);

    backupService.createBackupForScheduler();
  }



}
