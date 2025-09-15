package com.codeit.hrbank.batch.Processor;

import com.codeit.hrbank.entity.Employee;
import java.time.format.DateTimeFormatter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, String[]> {

  @Override
  public String[] process(Employee item) throws Exception {
    return new String[]{
        item.getId().toString(),
        item.getEmployeeNumber(),
        item.getName(),
        item.getEmail(),
        item.getDepartment().getName(),
        item.getPosition(),
        item.getHireDate().format(DateTimeFormatter.ISO_LOCAL_DATE).toString(),
        item.getStatus().toString(),

    };
  }
}
