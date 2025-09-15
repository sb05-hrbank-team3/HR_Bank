package com.codeit.hrbank.batch.Reader;


import com.codeit.hrbank.batch.job.JobConfig;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.repository.EmployeeRepository;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeReader implements ItemReader<Employee> {
  private final EmployeeRepository employeeRepository;
  private Iterator<Employee> iterator;

  @Override
  public Employee read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if(iterator == null){
      iterator = employeeRepository.findAll().iterator();
    }
    return iterator.hasNext() ? iterator.next() : null;
  }
}
