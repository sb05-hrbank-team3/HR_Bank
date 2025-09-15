package com.codeit.hrbank.batch.Reader;


import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.repository.ChangeLogRepository;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeLogReader implements ItemReader<ChangeLog> {
  private final ChangeLogRepository changeLogRepository;
  private Iterator<ChangeLog> iterator;

  @Override
  public ChangeLog read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if(iterator == null){
      iterator = changeLogRepository.findAll().iterator();
    }
    return iterator.hasNext() ? iterator.next() : null;
  }
}
