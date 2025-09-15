package com.codeit.hrbank.batch.Processor;

import com.codeit.hrbank.entity.History;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class HistoryProcessor implements ItemProcessor<History,String[]> {


  @Override
  public String[] process(History item) throws Exception {
    return new String[]{
        item.getId().toString(),
        item.getPropertyName(),
        item.getBefore(),
        item.getAfter(),
    };
  }
}
