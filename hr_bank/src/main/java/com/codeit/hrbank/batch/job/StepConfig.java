package com.codeit.hrbank.batch.job;


import com.codeit.hrbank.batch.Processor.ChangeLogProcessor;
import com.codeit.hrbank.batch.Processor.EmployeeProcessor;
import com.codeit.hrbank.batch.Processor.HistoryProcessor;
import com.codeit.hrbank.batch.Reader.ChangeLogReader;
import com.codeit.hrbank.batch.Reader.EmployeeReader;
import com.codeit.hrbank.batch.Reader.HistoryReader;
import com.codeit.hrbank.batch.writer.CsvWriter;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.History;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class StepConfig {

  private final EmployeeReader employeeReader;
  private final EmployeeProcessor employeeProcessor;

  private final ChangeLogReader changeLogReader;
  private final ChangeLogProcessor changeLogProcessor;

  private final HistoryReader historyReader;
  private final HistoryProcessor historyProcessor;

  private final CsvWriter csvWriter;


  @Bean
  public Step employeeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("employeeStep" , jobRepository)
        .<Employee, String[]> chunk(500,transactionManager)
        .reader(employeeReader)
        .processor(employeeProcessor)
        .writer(csvWriter)
        .build();
  }

  @Bean
  public Step changeLogStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("changeLogStep" , jobRepository)
        .<ChangeLog, String[]> chunk(500,transactionManager)
        .reader(changeLogReader)
        .processor(changeLogProcessor)
        .writer(csvWriter)
        .build();
  }

  @Bean
  public Step historyStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("historyStep" , jobRepository)
        .<History, String[]> chunk(500,transactionManager)
        .reader(historyReader)
        .processor(historyProcessor)
        .writer(csvWriter)
        .build();
  }


}
