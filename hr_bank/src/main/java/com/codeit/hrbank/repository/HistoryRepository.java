package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.History;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  HistoryRepository extends JpaRepository<History, Long> {

  List<History> findAllByChangeLogsId(Long logId);

}
