package com.codeit.hrbank.batch.Processor;

import com.codeit.hrbank.entity.ChangeLog;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ChangeLogProcessor implements ItemProcessor <ChangeLog, String[]>{


  @Override
  public String[] process(ChangeLog item) throws Exception {
    return new String[]{
        item.getId().toString(),
        item.getType().toString(),
        item.getIpAddress(),
        item.getEmployeeNumber(),
        item.getMemo()

    };
  }
}
