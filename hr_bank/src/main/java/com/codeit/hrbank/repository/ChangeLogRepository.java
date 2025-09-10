package com.codeit.hrbank.repository;

import com.codeit.hrbank.entity.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>, ChangeLogQueryRepository {

}
