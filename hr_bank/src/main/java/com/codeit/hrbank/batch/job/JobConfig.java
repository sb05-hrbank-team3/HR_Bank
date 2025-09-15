package com.codeit.hrbank.batch.job;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfig {
  private final Step employeeStep;
  private final Step changeLogStep;
  private final Step historyStep;


  @Bean
  public Job csvExportJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
    return new JobBuilder("csvExportJob" , jobRepository)
        .start(employeeStep)
        .next(changeLogStep)
        .next(historyStep)
        .build();
  }
}
