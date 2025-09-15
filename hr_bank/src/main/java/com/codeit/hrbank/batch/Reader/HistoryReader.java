package com.codeit.hrbank.batch.Reader;


import com.codeit.hrbank.entity.History;
import com.codeit.hrbank.repository.HistoryRepository;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryReader implements ItemReader<History> {
  private final HistoryRepository historyRepository;
  private Iterator<History> iterator;


  @Override
  public History read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if(iterator == null){
      iterator = historyRepository.findAll().iterator();
    }
    return iterator.hasNext() ? iterator.next() : null;
  }
}
