package com.codeit.hrbank.repository.impl;

import com.codeit.hrbank.dto.request.ChangeLogListRequest;
import com.codeit.hrbank.dto.response.ChangeLogListResponse;
import com.codeit.hrbank.repository.ChangeLogQueryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChangeLogQueryRepositoryImpl implements ChangeLogQueryRepository {
  private final EntityManager em;

  @Override
  public ChangeLogListResponse search(ChangeLogListRequest request) {
    return null;
  }
}
