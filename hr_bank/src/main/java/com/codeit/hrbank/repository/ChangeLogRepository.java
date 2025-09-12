package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.Employee;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>, ChangeLogQueryRepository {

  @Query("SELECT COUNT(c) FROM ChangeLog c WHERE c.at >= :from AND c.at <= :to")
  Long countBetween(@Param("from") Instant from, @Param("to") Instant to);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update ChangeLog cl set cl.employee = null where cl.employee = :employee")
  void unlinkEmployee(@Param("employee") Employee employee);
}
